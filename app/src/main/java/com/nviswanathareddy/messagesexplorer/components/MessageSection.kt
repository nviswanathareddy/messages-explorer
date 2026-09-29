package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nviswanathareddy.messagesexplorer.dialogs.MonthYearPickerDialog
import com.nviswanathareddy.messagesexplorer.model.MessageSort
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MessageSection(
    filteredMessages: List<SmsMessage>,
    listState: LazyListState,
    sortOption: MessageSort,
    onSortChange: (MessageSort) -> Unit,
    fontScale: Float,
    darkMode: Boolean,
    expandedMessageId: Long?,
    onMessageClick: (Long) -> Unit,
    hasSmsPermission: Boolean,
    onPermissionRequired: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit,
    palette: AppPalette,
    enableMonthPicker: Boolean,
    messageMonths: Set<Pair<Int, Int>>,
    onTodayClick: () -> Unit = {}
) {
    var sortMenuExpanded by remember {
        mutableStateOf(false)
    }

    var monthPickerOpen by remember {
        mutableStateOf(false)
    }

    var selectedMonthMillis by remember {
        mutableStateOf<Long?>(null)
    }

    val selectedMonthLabel = remember(selectedMonthMillis) {
        selectedMonthMillis?.let {
            SimpleDateFormat(
                "MMMM yyyy",
                Locale.getDefault()
            ).format(Date(it))
        } ?: "All Months"
    }

    val displayedMessages = if (
        enableMonthPicker && selectedMonthMillis != null
    ) {
        val selectedCalendar = Calendar.getInstance().apply {
            timeInMillis = selectedMonthMillis!!
        }

        filteredMessages.filter { message ->
            val messageCalendar = Calendar.getInstance().apply {
                timeInMillis = message.timestamp
            }

            messageCalendar.get(Calendar.YEAR) ==
                    selectedCalendar.get(Calendar.YEAR) &&
                    messageCalendar.get(Calendar.MONTH) ==
                    selectedCalendar.get(Calendar.MONTH)
        }
    } else {
        filteredMessages
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp,
                    vertical = 4.dp
                ),
            shape = RoundedCornerShape(12.dp),
            color = palette.dateCardBackground,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                palette.secondaryText.copy(alpha = 0.12f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    if (enableMonthPicker) {
                        Surface(
                            modifier = Modifier.clickable {
                                monthPickerOpen = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = palette.controlBackground,
                            shadowElevation = 1.dp,
                            border =
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    palette.secondaryText.copy(
                                        alpha = 0.12f
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.CalendarMonth,
                                    contentDescription =
                                        "Select month and year",
                                    modifier = Modifier.size(16.dp),
                                    tint = palette.primary
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(
                                    text = selectedMonthLabel,
                                    color = palette.primary,
                                    fontSize = scaledSp(
                                        12f,
                                        fontScale
                                    ),
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }
                        }
                    } else {
                        Surface(
                            modifier = Modifier.clickable {
                                onTodayClick()
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = palette.controlBackground,
                            shadowElevation = 1.dp,
                            border =
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    palette.secondaryText.copy(
                                        alpha = 0.12f
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.Today,
                                    contentDescription =
                                        "Jump to Today",
                                    modifier = Modifier.size(16.dp),
                                    tint = palette.primary
                                )

                                Spacer(
                                    modifier = Modifier.width(6.dp)
                                )

                                Text(
                                    text = "Today",
                                    color = palette.primary,
                                    fontSize = scaledSp(
                                        12f,
                                        fontScale
                                    ),
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Box {
                        Surface(
                            modifier = Modifier.clickable {
                                sortMenuExpanded = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = palette.controlBackground,
                            shadowElevation = 1.dp,
                            border =
                                androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    palette.secondaryText.copy(
                                        alpha = 0.12f
                                    )
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector =
                                        Icons.Outlined.SwapVert,
                                    contentDescription =
                                        "Sort messages",
                                    modifier = Modifier.size(16.dp),
                                    tint = palette.primary
                                )

                                Spacer(
                                    modifier = Modifier.width(4.dp)
                                )

                                Text(
                                    text = when (sortOption) {
                                        MessageSort.NEWEST_FIRST ->
                                            "Newest first"

                                        MessageSort.OLDEST_FIRST ->
                                            "Oldest first"

                                        MessageSort.SENDER_A_TO_Z ->
                                            "Sender A-Z"
                                    },
                                    color = palette.primary,
                                    fontSize = scaledSp(
                                        12f,
                                        fontScale
                                    ),
                                    fontWeight =
                                        FontWeight.SemiBold
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = {
                                sortMenuExpanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text("Newest first")
                                },
                                onClick = {
                                    onSortChange(
                                        MessageSort.NEWEST_FIRST
                                    )
                                    sortMenuExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Oldest first")
                                },
                                onClick = {
                                    onSortChange(
                                        MessageSort.OLDEST_FIRST
                                    )
                                    sortMenuExpanded = false
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text("Sender A-Z")
                                },
                                onClick = {
                                    onSortChange(
                                        MessageSort.SENDER_A_TO_Z
                                    )
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 2.dp
                        ),
                    horizontalArrangement =
                        Arrangement.Center,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = "TOTAL MESSAGES",
                        color = palette.secondaryText,
                        fontSize = scaledSp(
                            10f,
                            fontScale
                        ),
                        fontWeight =
                            FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Surface(
                        shape = CircleShape,
                        color = palette.dateControlBackground
                    ) {
                        Box(
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 2.dp
                            ),
                            contentAlignment =
                                Alignment.Center
                        ) {
                            Text(
                                text =
                                    displayedMessages.size.toString(),
                                color = palette.primary,
                                fontSize = scaledSp(
                                    11f,
                                    fontScale
                                ),
                                fontWeight =
                                    FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        if (!hasSmsPermission) {
            onPermissionRequired()
        } else if (displayedMessages.isEmpty()) {
            emptyContent()
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = displayedMessages,
                    key = { message ->
                        message.id
                    }
                ) { message ->
                    MessageCard(
                        message = message,
                        fontScale = fontScale,
                        darkMode = darkMode,
                        expanded =
                            expandedMessageId == message.id,
                        onClick = {
                            onMessageClick(message.id)
                        }
                    )
                }
            }
        }
    }

    if (enableMonthPicker && monthPickerOpen) {
        MonthYearPickerDialog(
            selectedMonthMillis = selectedMonthMillis,
            messageMonths = messageMonths,
            fontScale = fontScale,
            darkMode = darkMode,
            onDismiss = {
                monthPickerOpen = false
            },
            onSelectMonth = { monthMillis ->
                selectedMonthMillis = monthMillis
                monthPickerOpen = false
            }
        )
    }
}