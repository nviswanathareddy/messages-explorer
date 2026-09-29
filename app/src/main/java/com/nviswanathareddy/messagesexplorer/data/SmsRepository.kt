package com.nviswanathareddy.messagesexplorer.data

import android.content.Context
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.PhoneNumberUtils
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun readAllSms(
    context: Context,
    hasContactsPermission: Boolean
): List<SmsMessage> {
    val contactNames = if (hasContactsPermission) {
        loadContactNames(context)
    } else {
        emptyMap()
    }

    val result = mutableListOf<SmsMessage>()

    val projection = arrayOf(
        Telephony.Sms._ID,
        Telephony.Sms.ADDRESS,
        Telephony.Sms.BODY,
        Telephony.Sms.DATE
    )

    try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            null,
            null,
            "${Telephony.Sms.DATE} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(Telephony.Sms._ID)
            val addressIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val dateIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)

            while (cursor.moveToNext()) {
                val address = cursor.getString(addressIndex) ?: "Unknown"
                val normalizedAddress = PhoneNumberUtils.normalizeNumber(address)

                val sender = contactNames[normalizedAddress]
                    ?: normalizedAddress.takeIf { it.length >= 10 }?.let {
                        contactNames[it.takeLast(10)]
                    }
                    ?: address

                result.add(
                    SmsMessage(
                        id = cursor.getLong(idIndex),
                        sender = sender,
                        address = address,
                        body = cursor.getString(bodyIndex) ?: "",
                        timestamp = cursor.getLong(dateIndex)
                    )
                )
            }
        }
    } catch (_: SecurityException) {
        return emptyList()
    }

    return result
}

fun readSmsForDate(
    context: Context,
    selectedDateMillis: Long,
    hasContactsPermission: Boolean
): List<SmsMessage> {
    val endMillis = addDays(selectedDateMillis, 1)

    val contactNames = if (hasContactsPermission) {
        loadContactNames(context)
    } else {
        emptyMap()
    }

    val result = mutableListOf<SmsMessage>()

    val projection = arrayOf(
        Telephony.Sms._ID,
        Telephony.Sms.ADDRESS,
        Telephony.Sms.BODY,
        Telephony.Sms.DATE
    )

    val selection = "${Telephony.Sms.DATE} >= ? AND ${Telephony.Sms.DATE} < ?"

    val selectionArgs = arrayOf(
        selectedDateMillis.toString(),
        endMillis.toString()
    )

    try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${Telephony.Sms.DATE} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(Telephony.Sms._ID)
            val addressIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val dateIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)

            while (cursor.moveToNext()) {
                val address = cursor.getString(addressIndex) ?: "Unknown"
                val normalizedAddress = PhoneNumberUtils.normalizeNumber(address)

                val sender = contactNames[normalizedAddress]
                    ?: normalizedAddress.takeIf { it.length >= 10 }?.let {
                        contactNames[it.takeLast(10)]
                    }
                    ?: address

                result.add(
                    SmsMessage(
                        id = cursor.getLong(idIndex),
                        sender = sender,
                        address = address,
                        body = cursor.getString(bodyIndex) ?: "",
                        timestamp = cursor.getLong(dateIndex)
                    )
                )
            }
        }
    } catch (_: SecurityException) {
        return emptyList()
    }

    return result
}

fun loadMessageDaysForMonth(
    context: Context,
    monthMillis: Long
): Set<Int> {
    val startMillis = firstDayOfMonth(monthMillis)
    val endMillis = addMonths(startMillis, 1)
    val days = mutableSetOf<Int>()

    val projection = arrayOf(
        Telephony.Sms.DATE
    )

    val selection = "${Telephony.Sms.DATE} >= ? AND ${Telephony.Sms.DATE} < ?"

    val selectionArgs = arrayOf(
        startMillis.toString(),
        endMillis.toString()
    )

    return try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            val dateIndex = cursor.getColumnIndexOrThrow(
                Telephony.Sms.DATE
            )

            while (cursor.moveToNext()) {
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = cursor.getLong(dateIndex)
                }

                days.add(
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            }

            days
        } ?: emptySet()
    } catch (_: SecurityException) {
        emptySet()
    }
}

fun loadContactNames(
    context: Context
): Map<String, String> {
    val exactMatches = mutableMapOf<String, String>()
    val lastTenCandidates = mutableMapOf<String, MutableSet<String>>()

    return try {
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

            val numberIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)
                    ?.takeIf { it.isNotBlank() }
                    ?: continue

                val number = cursor.getString(numberIndex)
                    ?: continue

                val normalized = PhoneNumberUtils.normalizeNumber(number)

                if (normalized.isBlank()) {
                    continue
                }

                exactMatches[normalized] = name

                if (normalized.length >= 10) {
                    lastTenCandidates
                        .getOrPut(
                            normalized.takeLast(10)
                        ) {
                            mutableSetOf()
                        }
                        .add(name)
                }
            }

            val result = exactMatches.toMutableMap()

            lastTenCandidates.forEach { (lastTen, names) ->
                if (names.size == 1 && !result.containsKey(lastTen)) {
                    result[lastTen] = names.first()
                }
            }

            result
        } ?: emptyMap()
    } catch (_: SecurityException) {
        emptyMap()
    }
}

fun startOfDayMillis(calendar: Calendar): Long {
    return (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

fun firstDayOfMonth(millis: Long): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        set(Calendar.DAY_OF_MONTH, 1)
    }

    return startOfDayMillis(calendar)
}

fun addDays(
    millis: Long,
    days: Int
): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.DAY_OF_MONTH, days)
    }

    return startOfDayMillis(calendar)
}

fun addMonths(
    millis: Long,
    months: Int
): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.MONTH, months)
        set(Calendar.DAY_OF_MONTH, 1)
    }

    return startOfDayMillis(calendar)
}

fun formatTime(millis: Long): String {
    return SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    ).format(Date(millis))
}

fun loadMessageMonths(
    context: Context,
    hasContactsPermission: Boolean
): Set<Pair<Int, Int>> {
    val result = mutableSetOf<Pair<Int, Int>>()

    val projection = arrayOf(
        Telephony.Sms.DATE
    )

    try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            null,
            null,
            "${Telephony.Sms.DATE} ASC"
        )?.use { cursor ->
            val dateIndex = cursor.getColumnIndexOrThrow(
                Telephony.Sms.DATE
            )

            while (cursor.moveToNext()) {
                val timestamp = cursor.getLong(dateIndex)

                val calendar = Calendar.getInstance().apply {
                    timeInMillis = timestamp
                }

                result.add(
                    Pair(
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH)
                    )
                )
            }
        }
    } catch (_: SecurityException) {
        return emptySet()
    }

    return result
}