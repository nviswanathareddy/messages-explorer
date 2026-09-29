package com.nviswanathareddy.messagesexplorer.dialogs

import android.content.Context
import android.provider.Telephony
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppFontMenu
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val DateDialogPadding = 20.dp
private val DateDialogRadius = 16.dp
private val DateCellSize = 36.dp
private val DateCellRadius = 18.dp
private val DateButtonHeight = 44.dp

@Composable
fun CustomDatePickerDialog(
    selectedDateMillis: Long,
    hasSmsPermission: Boolean,
    fontScale: Float,
    darkMode: Boolean,
    onDismiss: () -> Unit,
    onSelectDate: (Long) -> Unit
) {
    val palette: AppPalette =
        if (darkMode) {
            DarkPalette
        } else {
            LightPalette
        }

    val context =
        LocalContext.current

    val locale =
        remember {
            Locale.getDefault()
        }

    var pickerDateMillis by remember {
        mutableLongStateOf(
            startOfDayMillis(
                selectedDateMillis
            )
        )
    }

    var visibleMonthMillis by remember {
        mutableLongStateOf(
            firstDayOfMonth(
                selectedDateMillis
            )
        )
    }

    var yearMenuExpanded by remember {
        mutableStateOf(false)
    }

    val messageDays: Set<Int> =
        remember(
            visibleMonthMillis,
            hasSmsPermission
        ) {
            if (hasSmsPermission) {
                loadMessageDaysForMonth(
                    context,
                    visibleMonthMillis
                )
            } else {
                emptySet()
            }
        }

    val selectedCalendar =
        Calendar.getInstance().apply {
            timeInMillis =
                pickerDateMillis
        }

    val visibleCalendar =
        Calendar.getInstance().apply {
            timeInMillis =
                visibleMonthMillis
        }

    val currentCalendar =
        Calendar.getInstance()

    val currentYear =
        currentCalendar.get(
            Calendar.YEAR
        )

    val currentMonth =
        currentCalendar.get(
            Calendar.MONTH
        )

    val currentDay =
        currentCalendar.get(
            Calendar.DAY_OF_MONTH
        )

    val selectedWeekday =
        SimpleDateFormat(
            "EEEE",
            locale
        ).format(
            Date(
                pickerDateMillis
            )
        )

    val selectedDateLabel =
        SimpleDateFormat(
            "dd MMMM yyyy",
            locale
        ).format(
            Date(
                pickerDateMillis
            )
        )

    val monthYearLabel =
        SimpleDateFormat(
            "MMMM yyyy",
            locale
        ).format(
            Date(
                visibleMonthMillis
            )
        )

    val days =
        buildCalendarDays(
            visibleMonthMillis
        )

    val weekLabels =
        listOf(
            "M",
            "T",
            "W",
            "T",
            "F",
            "S",
            "S"
        )

    val selectableYears =
        (2000..(currentYear + 1))
            .toList()
            .reversed()

    AppDialog(
        palette = palette,
        onDismiss = onDismiss
    ) { dismiss ->

        Column(
            modifier =
                Modifier.fillMaxWidth()
        ) {

            // -----------------------------------------------------
            // SELECTED DATE HEADER
            // -----------------------------------------------------

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            DateDialogPadding
                        )
                        .border(
                            width = 0.dp,
                            color = Color.Transparent
                        )
            ) {

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text =
                            "SELECT DATE",
                        color =
                            palette.primary,
                        fontSize =
                            scaledSp(
                                10f,
                                fontScale
                            ),
                        fontWeight =
                            FontWeight.Bold
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(5.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {
                        Text(
                            text =
                                selectedWeekday,
                            color =
                                palette.primary,
                            fontSize =
                                scaledSp(
                                    12f,
                                    fontScale
                                ),
                            fontWeight =
                                FontWeight.Medium
                        )

                        Spacer(
                            modifier =
                                Modifier.height(2.dp)
                        )

                        Text(
                            text =
                                selectedDateLabel,
                            color =
                                palette.primaryDark,
                            fontSize =
                                scaledSp(
                                    20f,
                                    fontScale
                                ),
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }

                    Surface(
                        modifier =
                            Modifier.clickable {
                                pickerDateMillis =
                                    startOfDayMillis(
                                        System.currentTimeMillis()
                                    )

                                visibleMonthMillis =
                                    firstDayOfMonth(
                                        pickerDateMillis
                                    )
                            },
                        shape =
                            RoundedCornerShape(
                                12.dp
                            ),
                        color =
                            palette.controlBackground
                    ) {
                        Text(
                            text =
                                "Jump to Today",
                            modifier =
                                Modifier.padding(
                                    horizontal = 14.dp,
                                    vertical = 8.dp
                                ),
                            color =
                                palette.primary,
                            fontSize =
                                scaledSp(
                                    12f,
                                    fontScale
                                ),
                            fontWeight =
                                FontWeight.Medium
                        )
                    }
                }
            }

            // -----------------------------------------------------
            // CALENDAR CONTENT
            // -----------------------------------------------------

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = DateDialogPadding,
                            end = DateDialogPadding,
                            top = 12.dp
                        )
            ) {

                // -------------------------------------------------
                // MONTH / YEAR HEADER
                // -------------------------------------------------

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                vertical = 4.dp
                            ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box {

                        Row(
                            modifier =
                                Modifier.clickable {
                                    yearMenuExpanded =
                                        true
                                },
                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {
                            Text(
                                text =
                                    monthYearLabel,
                                color =
                                    palette.primaryDark,
                                fontSize =
                                    scaledSp(
                                        16f,
                                        fontScale
                                    ),
                                fontWeight =
                                    FontWeight.SemiBold
                            )

                            Icon(
                                imageVector =
                                    Icons.Outlined.ExpandMore,
                                contentDescription =
                                    "Select year",
                                tint =
                                    palette.secondaryText,
                                modifier =
                                    Modifier.size(
                                        20.dp
                                    )
                            )
                        }

                        DropdownMenu(
                            expanded =
                                yearMenuExpanded,
                            onDismissRequest = {
                                yearMenuExpanded =
                                    false
                            }
                        ) {
                            selectableYears.forEach { year ->

                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text =
                                                year.toString(),
                                            fontSize =
                                                scaledSp(
                                                    AppFontMenu,
                                                    fontScale
                                                )
                                        )
                                    },
                                    onClick = {

                                        val updated =
                                            Calendar
                                                .getInstance()
                                                .apply {
                                                    timeInMillis =
                                                        visibleMonthMillis

                                                    set(
                                                        Calendar.YEAR,
                                                        year
                                                    )

                                                    set(
                                                        Calendar.DAY_OF_MONTH,
                                                        1
                                                    )

                                                    set(
                                                        Calendar.HOUR_OF_DAY,
                                                        0
                                                    )

                                                    set(
                                                        Calendar.MINUTE,
                                                        0
                                                    )

                                                    set(
                                                        Calendar.SECOND,
                                                        0
                                                    )

                                                    set(
                                                        Calendar.MILLISECOND,
                                                        0
                                                    )
                                                }

                                        visibleMonthMillis =
                                            firstDayOfMonth(
                                                updated.timeInMillis
                                            )

                                        yearMenuExpanded =
                                            false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.weight(1f)
                    )

                    Row(
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        IconButton(
                            onClick = {
                                visibleMonthMillis =
                                    addMonths(
                                        visibleMonthMillis,
                                        -1
                                    )
                            },
                            modifier =
                                Modifier.size(
                                    32.dp
                                )
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.ChevronLeft,
                                contentDescription =
                                    "Previous month",
                                tint =
                                    palette.secondaryText,
                                modifier =
                                    Modifier.size(
                                        20.dp
                                    )
                            )
                        }

                        IconButton(
                            onClick = {
                                visibleMonthMillis =
                                    addMonths(
                                        visibleMonthMillis,
                                        1
                                    )
                            },
                            modifier =
                                Modifier.size(
                                    32.dp
                                )
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.ChevronRight,
                                contentDescription =
                                    "Next month",
                                tint =
                                    palette.secondaryText,
                                modifier =
                                    Modifier.size(
                                        20.dp
                                    )
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                // -------------------------------------------------
                // WEEK LABELS
                // -------------------------------------------------

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(
                            4.dp
                        )
                ) {
                    weekLabels.forEach { label ->

                        Box(
                            modifier =
                                Modifier.weight(1f),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Text(
                                text =
                                    label,
                                color =
                                    palette.secondaryText,
                                fontSize =
                                    scaledSp(
                                        12f,
                                        fontScale
                                    ),
                                fontWeight =
                                    FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(6.dp)
                )

                // -------------------------------------------------
                // CALENDAR GRID
                // -------------------------------------------------

                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(
                            8.dp
                        )
                ) {

                    days.chunked(7).forEach { week ->

                        Row(
                            modifier =
                                Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    4.dp
                                )
                        ) {

                            week.forEach { day ->

                                Box(
                                    modifier =
                                        Modifier
                                            .weight(1f)
                                            .height(
                                                DateCellSize
                                            ),
                                    contentAlignment =
                                        Alignment.Center
                                ) {

                                    if (day == null) {
                                        Spacer(
                                            modifier =
                                                Modifier.size(
                                                    DateCellSize
                                                )
                                        )
                                    } else {

                                        val dayNumber =
                                            day.get(
                                                Calendar.DAY_OF_MONTH
                                            )

                                        val dayMonth =
                                            day.get(
                                                Calendar.MONTH
                                            )

                                        val dayYear =
                                            day.get(
                                                Calendar.YEAR
                                            )

                                        val isCurrentMonth =
                                            dayMonth ==
                                                    visibleCalendar.get(
                                                        Calendar.MONTH
                                                    ) &&
                                                    dayYear ==
                                                    visibleCalendar.get(
                                                        Calendar.YEAR
                                                    )

                                        val isSelected =
                                            sameDay(
                                                day,
                                                selectedCalendar
                                            )

                                        val isToday =
                                            dayYear ==
                                                    currentYear &&
                                                    dayMonth ==
                                                    currentMonth &&
                                                    dayNumber ==
                                                    currentDay

                                        val hasMessages =
                                            isCurrentMonth &&
                                                    messageDays.contains(
                                                        dayNumber
                                                    )

                                        DateItem(
                                            day =
                                                dayNumber,
                                            isCurrentMonth =
                                                isCurrentMonth,
                                            isSelected =
                                                isSelected,
                                            isToday =
                                                isToday,
                                            hasMessages =
                                                hasMessages,
                                            palette =
                                                palette,
                                            fontScale =
                                                fontScale,
                                            onClick = {

                                                pickerDateMillis =
                                                    startOfDayMillis(
                                                        day
                                                    )

                                                if (!isCurrentMonth) {
                                                    visibleMonthMillis =
                                                        firstDayOfMonth(
                                                            day.timeInMillis
                                                        )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )
            }

            // -----------------------------------------------------
            // FOOTER
            // -----------------------------------------------------

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal =
                                DateDialogPadding,
                            vertical = 12.dp
                        ),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Surface(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(
                                DateButtonHeight
                            )
                            .clickable {
                                dismiss()
                            },
                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),
                    color =
                        palette.controlBackground
                ) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                                "Cancel",
                            color =
                                palette.secondaryText,
                            fontSize =
                                scaledSp(
                                    14f,
                                    fontScale
                                ),
                            fontWeight =
                                FontWeight.Medium
                        )
                    }
                }

                Surface(
                    modifier =
                        Modifier
                            .weight(1f)
                            .height(
                                DateButtonHeight
                            )
                            .clickable {
                                onSelectDate(
                                    pickerDateMillis
                                )
                                dismiss()
                            },
                    shape =
                        RoundedCornerShape(
                            12.dp
                        ),
                    color =
                        palette.primary
                ) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Text(
                            text =
                                "Select Date",
                            color =
                                Color.White,
                            fontSize =
                                scaledSp(
                                    14f,
                                    fontScale
                                ),
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DateItem(
    day: Int,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    isToday: Boolean,
    hasMessages: Boolean,
    palette: AppPalette,
    fontScale: Float,
    onClick: () -> Unit
) {
    val background =
        when {
            isSelected ->
                palette.primary

            isToday ->
                palette.controlBackground

            else ->
                Color.Transparent
        }

    val textColor =
        when {
            isSelected ->
                Color.White

            !isCurrentMonth ->
                palette.secondaryText.copy(
                    alpha = 0.35f
                )

            else ->
                palette.primaryDark
        }

    Surface(
        modifier =
            Modifier
                .size(
                    DateCellSize
                )
                .clickable {
                    onClick()
                },
        shape =
            CircleShape,
        color =
            background
    ) {
        Box(
            contentAlignment =
                Alignment.Center
        ) {

            Text(
                text =
                    day.toString(),
                color =
                    textColor,
                fontSize =
                    scaledSp(
                        if (isSelected) {
                            16f
                        } else {
                            14f
                        },
                        fontScale
                    ),
                fontWeight =
                    when {
                        isSelected ->
                            FontWeight.SemiBold

                        isToday ->
                            FontWeight.Medium

                        else ->
                            FontWeight.Normal
                    }
            )

            if (hasMessages) {

                Surface(
                    modifier =
                        Modifier
                            .size(
                                if (isSelected) {
                                    6.dp
                                } else {
                                    4.dp
                                }
                            )
                            .align(
                                Alignment.BottomCenter
                            )
                            .padding(
                                bottom =
                                    if (isSelected) {
                                        4.dp
                                    } else {
                                        3.dp
                                    }
                            ),
                    shape =
                        CircleShape,
                    color =
                        if (isSelected) {
                            Color.White
                        } else {
                            palette.primary
                        }
                ) {}
            }
        }
    }
}

// =================================================================
// SMS MESSAGE DAYS
// =================================================================

private fun loadMessageDaysForMonth(
    context: Context,
    monthMillis: Long
): Set<Int> {
    val startMillis =
        firstDayOfMonth(
            monthMillis
        )

    val endMillis =
        addMonths(
            startMillis,
            1
        )

    val messageDays =
        mutableSetOf<Int>()

    val projection =
        arrayOf(
            Telephony.Sms.DATE
        )

    val selection =
        "${Telephony.Sms.DATE} >= ? AND " +
                "${Telephony.Sms.DATE} < ?"

    val selectionArgs =
        arrayOf(
            startMillis.toString(),
            endMillis.toString()
        )

    try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->

            val dateIndex =
                cursor.getColumnIndexOrThrow(
                    Telephony.Sms.DATE
                )

            while (
                cursor.moveToNext()
            ) {
                val timestamp =
                    cursor.getLong(
                        dateIndex
                    )

                val calendar =
                    Calendar.getInstance().apply {
                        timeInMillis =
                            timestamp
                    }

                messageDays.add(
                    calendar.get(
                        Calendar.DAY_OF_MONTH
                    )
                )
            }
        }
    } catch (
        _: SecurityException
    ) {
        return emptySet()
    }

    return messageDays
}

// =================================================================
// CALENDAR HELPERS
// =================================================================

private fun firstDayOfMonth(
    millis: Long
): Long {
    return Calendar.getInstance().apply {
        timeInMillis =
            millis

        set(
            Calendar.DAY_OF_MONTH,
            1
        )

        set(
            Calendar.HOUR_OF_DAY,
            0
        )

        set(
            Calendar.MINUTE,
            0
        )

        set(
            Calendar.SECOND,
            0
        )

        set(
            Calendar.MILLISECOND,
            0
        )
    }.timeInMillis
}

private fun startOfDayMillis(
    millis: Long
): Long {
    return Calendar.getInstance().apply {
        timeInMillis =
            millis

        set(
            Calendar.HOUR_OF_DAY,
            0
        )

        set(
            Calendar.MINUTE,
            0
        )

        set(
            Calendar.SECOND,
            0
        )

        set(
            Calendar.MILLISECOND,
            0
        )
    }.timeInMillis
}

private fun startOfDayMillis(
    calendar: Calendar
): Long {
    return startOfDayMillis(
        calendar.timeInMillis
    )
}

private fun addMonths(
    millis: Long,
    months: Int
): Long {
    return Calendar.getInstance().apply {
        timeInMillis =
            millis

        add(
            Calendar.MONTH,
            months
        )
    }.timeInMillis
}

private fun buildCalendarDays(
    monthMillis: Long
): List<Calendar?> {
    val month =
        Calendar.getInstance().apply {
            timeInMillis =
                monthMillis

            set(
                Calendar.DAY_OF_MONTH,
                1
            )
        }

    val firstDay =
        month.get(
            Calendar.DAY_OF_WEEK
        )

    val mondayBasedOffset =
        (
                firstDay + 5
                ) % 7

    val daysInMonth =
        month.getActualMaximum(
            Calendar.DAY_OF_MONTH
        )

    val result =
        mutableListOf<Calendar?>()

    repeat(
        mondayBasedOffset
    ) {
        val previousDay =
            Calendar.getInstance().apply {
                timeInMillis =
                    month.timeInMillis

                add(
                    Calendar.DAY_OF_MONTH,
                    -(
                            mondayBasedOffset -
                                    it
                            )
                )
            }

        result.add(
            previousDay
        )
    }

    for (
    dayNumber in
    1..daysInMonth
    ) {
        result.add(
            Calendar.getInstance().apply {
                timeInMillis =
                    month.timeInMillis

                set(
                    Calendar.DAY_OF_MONTH,
                    dayNumber
                )
            }
        )
    }

    var nextDay =
        1

    while (
        result.size < 42
    ) {
        val nextCalendar =
            Calendar.getInstance().apply {
                timeInMillis =
                    month.timeInMillis

                add(
                    Calendar.MONTH,
                    1
                )

                set(
                    Calendar.DAY_OF_MONTH,
                    nextDay
                )
            }

        result.add(
            nextCalendar
        )

        nextDay++
    }

    return result
}

private fun sameDay(
    first: Calendar,
    second: Calendar
): Boolean {
    return first.get(
        Calendar.YEAR
    ) ==
            second.get(
                Calendar.YEAR
            ) &&
            first.get(
                Calendar.DAY_OF_YEAR
            ) ==
            second.get(
                Calendar.DAY_OF_YEAR
            )
}