package com.nviswanathareddy.messagesexplorer.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

private val MonthYearDialogPadding = 20.dp
private val MonthYearDialogRadius = 12.dp
private val MonthCircleSize = 40.dp
private val MonthDotSize = 5.dp
private val YearSelectorHeight = 36.dp
private val ActionButtonHeight = 48.dp
private val ActionButtonRadius = 8.dp
private val HeaderLabelSize = 10f
private val HeaderMonthSize = 20f

@Composable
fun MonthYearPickerDialog(
    selectedMonthMillis: Long?,
    messageMonths: Set<Pair<Int, Int>>,
    fontScale: Float,
    darkMode: Boolean,
    onDismiss: () -> Unit,
    onSelectMonth: (Long?) -> Unit
) {
    val palette: AppPalette = if (darkMode) DarkPalette else LightPalette
    val locale = LocalLocale.current.platformLocale
    val initialCalendar =
        remember(selectedMonthMillis) {
            Calendar.getInstance().apply {
                timeInMillis = selectedMonthMillis ?: System.currentTimeMillis()
                set(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
        }
    var visibleYear by remember { mutableIntStateOf(initialCalendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableIntStateOf(initialCalendar.get(Calendar.MONTH)) }
    val currentCalendar = remember { Calendar.getInstance() }
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentMonth = currentCalendar.get(Calendar.MONTH)
    val monthNames = remember {
        listOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )
    }
    val monthIndices = remember { 0 until 12 }
    val selectedMonthMillisForDisplay =
        remember(visibleYear, selectedMonth) {
            Calendar.getInstance()
                .apply {
                    set(visibleYear, selectedMonth, 1, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                .timeInMillis
        }
    val monthYearFormatter = remember(locale) { SimpleDateFormat("MMMM yyyy", locale) }
    val selectedMonthLabel = monthYearFormatter.format(Date(selectedMonthMillisForDisplay))
    val selectableYears = remember(currentYear) { (2000..(currentYear + 1)).toList().reversed() }
    AppDialog(palette = palette, onDismiss = onDismiss) { dismiss ->
        Column(modifier = Modifier.fillMaxWidth()) {
            // HEADER
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = palette.dateCardBackground,
                shape =
                    RoundedCornerShape(
                        topStart = MonthYearDialogRadius,
                        topEnd = MonthYearDialogRadius
                    )
            ) {
                Column(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(
                                start = MonthYearDialogPadding,
                                top = 16.dp,
                                end = MonthYearDialogPadding,
                                bottom = 16.dp
                            )
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SELECT MONTH & YEAR",
                            color = palette.primary,
                            fontSize = scaledSp(HeaderLabelSize, fontScale),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.4.sp
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Surface(
                            modifier =
                                Modifier.clickable {
                                    onSelectMonth(null)
                                    dismiss()
                                },
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White
                        ) {
                            Text(
                                text = "All Months",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = palette.primary,
                                fontSize = scaledSp(12f, fontScale),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = selectedMonthLabel,
                        color = palette.primaryDark,
                        fontSize = scaledSp(HeaderMonthSize, fontScale),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // YEAR SELECTOR
            Column(
                modifier =
                    Modifier.fillMaxWidth()
                        .padding(
                            start = MonthYearDialogPadding,
                            top = 16.dp,
                            end = MonthYearDialogPadding
                        )
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth().height(YearSelectorHeight),
                    shape = RoundedCornerShape(8.dp),
                    color = palette.controlBackground,
                    border = BorderStroke(width = 1.dp, color = palette.primary.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier =
                                Modifier.weight(1f).fillMaxHeight().clickable { visibleYear-- },
                            shape = RoundedCornerShape(0.dp),
                            color = Color.White
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChevronLeft,
                                    contentDescription = "Previous year",
                                    tint = palette.secondaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier.weight(1f).fillMaxHeight(),
                            shape = RoundedCornerShape(0.dp),
                            color = palette.controlBackground
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = visibleYear.toString(),
                                    color = palette.primaryDark,
                                    fontSize = scaledSp(12f, fontScale),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Surface(
                            modifier =
                                Modifier.weight(1f).fillMaxHeight().clickable { visibleYear++ },
                            shape = RoundedCornerShape(0.dp),
                            color = Color.White
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = "Next year",
                                    tint = palette.secondaryText,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // MONTH GRID
                Column(modifier = Modifier.fillMaxWidth().height(224.dp)) {
                    for (row in 0 until 4) {
                        Row(
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (column in 0 until 3) {
                                val monthIndex = row * 3 + column
                                val hasMessages = messageMonths.contains(visibleYear to monthIndex)
                                val isSelected = selectedMonth == monthIndex
                                val isCurrentMonth =
                                    visibleYear == currentYear && monthIndex == currentMonth
                                MonthItem(
                                    monthName = monthNames[monthIndex],
                                    selected = isSelected,
                                    current = isCurrentMonth,
                                    hasMessages = hasMessages,
                                    palette = palette,
                                    fontScale = fontScale,
                                    modifier = Modifier.weight(1f),
                                    onClick = { selectedMonth = monthIndex }
                                )
                            }
                        }
                    }
                }
                // DIVIDER
                Surface(
                    modifier = Modifier.fillMaxWidth().height(1.dp),
                    color = palette.secondaryText.copy(alpha = 0.12f)
                ) {}
                // FOOTER
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier =
                            Modifier.weight(1f).height(ActionButtonHeight).clickable { dismiss() },
                        shape = RoundedCornerShape(ActionButtonRadius),
                        color = palette.controlBackground
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Cancel",
                                color = palette.secondaryText,
                                fontSize = scaledSp(14f, fontScale),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Surface(
                        modifier =
                            Modifier.weight(1f).height(ActionButtonHeight).clickable {
                                onSelectMonth(selectedMonthMillisForDisplay)
                                dismiss()
                            },
                        shape = RoundedCornerShape(ActionButtonRadius),
                        color = palette.primary
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select Month",
                                color = Color.White,
                                fontSize = scaledSp(14f, fontScale),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthItem(
    monthName: String,
    selected: Boolean,
    current: Boolean,
    hasMessages: Boolean,
    palette: AppPalette,
    fontScale: Float,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val background =
        when {
            selected -> palette.primary
            current -> palette.dateControlBackground
            else -> Color.Transparent
        }
    val textColor = if (selected) Color.White else palette.primaryDark
    Box(
        modifier = modifier.height(56.dp).clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier.size(MonthCircleSize),
            shape = CircleShape,
            color = background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = monthName.take(3),
                    color = textColor,
                    fontSize = scaledSp(12f, fontScale),
                    fontWeight =
                        if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Medium
                        }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Surface(
                    modifier = Modifier.size(MonthDotSize),
                    shape = CircleShape,
                    color =
                        when {
                            selected -> Color.White
                            hasMessages -> palette.primary
                            else -> Color.Transparent
                        }
                ) {}
            }
        }
    }
}
