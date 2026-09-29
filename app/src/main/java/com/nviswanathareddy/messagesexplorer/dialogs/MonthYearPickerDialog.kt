package com.nviswanathareddy.messagesexplorer.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
private val YearContainerRadius = 12.dp
private val MonthItemRadius = 12.dp
private val MonthItemHeight = 40.dp
private val YearButtonSize = 32.dp
private val MonthDotSize = 6.dp

@Composable
fun MonthYearPickerDialog(
    selectedMonthMillis: Long?,
    messageMonths: Set<Pair<Int, Int>>,
    fontScale: Float,
    darkMode: Boolean,
    onDismiss: () -> Unit,
    onSelectMonth: (Long?) -> Unit
) {
    val palette: AppPalette =
        if (darkMode) {
            DarkPalette
        } else {
            LightPalette
        }

    val initialCalendar =
        remember(selectedMonthMillis) {
            Calendar.getInstance().apply {
                timeInMillis =
                    selectedMonthMillis
                        ?: System.currentTimeMillis()

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
        }

    var visibleYear by remember {
        mutableStateOf(
            initialCalendar.get(
                Calendar.YEAR
            )
        )
    }

    var selectedMonth by remember {
        mutableStateOf(
            initialCalendar.get(
                Calendar.MONTH
            )
        )
    }

    val currentCalendar =
        remember {
            Calendar.getInstance()
        }

    val currentYear =
        currentCalendar.get(
            Calendar.YEAR
        )

    val monthNames =
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

    val selectedMonthMillisForDisplay =
        remember(
            visibleYear,
            selectedMonth
        ) {
            Calendar.getInstance().apply {
                set(
                    Calendar.YEAR,
                    visibleYear
                )
                set(
                    Calendar.MONTH,
                    selectedMonth
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
            }.timeInMillis
        }

    val selectedMonthLabel =
        SimpleDateFormat(
            "MMMM yyyy",
            LocalLocale.current.platformLocale
        ).format(
            Date(
                selectedMonthMillisForDisplay
            )
        )

    val isCurrentYear =
        visibleYear == currentYear

    AppDialog(
        palette = palette,
        onDismiss = onDismiss
    ) { dismiss ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    MonthYearDialogPadding
                ),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 12.dp
                    )
            ) {
                Text(
                    text = "SELECT MONTH & YEAR",
                    color = palette.primary,
                    fontSize = scaledSp(
                        10f,
                        fontScale
                    ),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedMonthLabel,
                        color = palette.primaryDark,
                        fontSize = scaledSp(
                            18f,
                            fontScale
                        ),
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        modifier = Modifier.clickable {
                            onSelectMonth(null)
                            dismiss()
                        },
                        shape = RoundedCornerShape(
                            8.dp
                        ),
                        color =
                            palette.dateControlBackground
                    ) {
                        Text(
                            text = "All Months",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
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

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    YearContainerRadius
                ),
                color = palette.controlBackground
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 12.dp,
                            vertical = 8.dp
                        ),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(
                            YearButtonSize
                        ),
                        shape = RoundedCornerShape(
                            8.dp
                        ),
                        color = Color.Transparent
                    ) {
                        IconButton(
                            onClick = {
                                visibleYear--
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.ChevronLeft,
                                contentDescription =
                                    "Previous year",
                                tint =
                                    palette.secondaryText,
                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = visibleYear.toString(),
                        color = palette.primaryDark,
                        fontSize = scaledSp(
                            14f,
                            fontScale
                        ),
                        fontWeight =
                            FontWeight.Bold
                    )

                    if (isCurrentYear) {
                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Surface(
                            shape = RoundedCornerShape(
                                50
                            ),
                            color =
                                palette.primary.copy(
                                    alpha = 0.12f
                                )
                        ) {
                            Text(
                                text = "Current",
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 2.dp
                                ),
                                color = palette.primary,
                                fontSize = scaledSp(
                                    10f,
                                    fontScale
                                ),
                                fontWeight =
                                    FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.weight(1f)
                    )

                    Surface(
                        modifier = Modifier.size(
                            YearButtonSize
                        ),
                        shape = RoundedCornerShape(
                            8.dp
                        ),
                        color = Color.Transparent
                    ) {
                        IconButton(
                            onClick = {
                                visibleYear++
                            }
                        ) {
                            Icon(
                                imageVector =
                                    Icons.Outlined.ChevronRight,
                                contentDescription =
                                    "Next year",
                                tint =
                                    palette.secondaryText,
                                modifier =
                                    Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0 until 4) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        for (column in 0 until 3) {
                            val monthIndex =
                                row * 3 + column

                            val monthHasMessages =
                                messageMonths.contains(
                                    Pair(
                                        visibleYear,
                                        monthIndex
                                    )
                                )

                            val isSelected =
                                selectedMonth ==
                                        monthIndex

                            MonthItem(
                                monthName =
                                    monthNames[
                                        monthIndex
                                    ],
                                selected =
                                    isSelected,
                                hasMessages =
                                    monthHasMessages,
                                palette =
                                    palette,
                                fontScale =
                                    fontScale,
                                modifier =
                                    Modifier.weight(1f),
                                onClick = {
                                    selectedMonth =
                                        monthIndex
                                }
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 12.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(12.dp),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable {
                            dismiss()
                        },
                    shape = RoundedCornerShape(
                        12.dp
                    ),
                    color = palette.controlBackground
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
                            text = "Cancel",
                            color =
                                palette.secondaryText,
                            fontSize = scaledSp(
                                14f,
                                fontScale
                            ),
                            fontWeight =
                                FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .clickable {
                            onSelectMonth(
                                selectedMonthMillisForDisplay
                            )
                            dismiss()
                        },
                    shape = RoundedCornerShape(
                        12.dp
                    ),
                    color = palette.primary
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
                            text = "Select Month",
                            color = Color.White,
                            fontSize = scaledSp(
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
private fun MonthItem(
    monthName: String,
    selected: Boolean,
    hasMessages: Boolean,
    palette: AppPalette,
    fontScale: Float,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .height(
                MonthItemHeight
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(
            MonthItemRadius
        ),
        color =
            if (selected) {
                palette.primary
            } else {
                Color.Transparent
            }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.Center
        ) {
            Text(
                text = monthName.take(3),
                color =
                    if (selected) {
                        Color.White
                    } else {
                        palette.primaryDark
                    },
                fontSize = scaledSp(
                    14f,
                    fontScale
                ),
                fontWeight =
                    if (selected) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Medium
                    }
            )

            Spacer(
                modifier = Modifier.height(2.dp)
            )

            Surface(
                modifier = Modifier.size(
                    MonthDotSize
                ),
                shape = RoundedCornerShape(
                    50
                ),
                color =
                    when {
                        selected ->
                            Color.White

                        hasMessages ->
                            palette.primary

                        else ->
                            Color.Transparent
                    }
            ) {}
        }
    }
}