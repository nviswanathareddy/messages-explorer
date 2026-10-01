package com.nviswanathareddy.messagesexplorer.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.SettingsSuggest
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.AppThemeMode
import com.nviswanathareddy.messagesexplorer.AppTimeFormat
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

private val DialogContentPadding = 20.dp
private val DialogSectionSpacing = 16.dp
private val DialogSectionTitleSpacing = 8.dp
private val DialogSegmentHeight = 40.dp
private val DialogSegmentRadius = 8.dp
private val DialogActionHeight = 40.dp
private val DialogActionRadius = 8.dp
private val DialogCloseSize = 32.dp
private val DialogDividerAlpha = 0.18f

@Composable
fun SettingsDialog(
    darkMode: Boolean,
    fontScale: Float,
    themeMode: AppThemeMode,
    timeFormat: AppTimeFormat,
    onThemeModeChange: (AppThemeMode) -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onTimeFormatChange: (AppTimeFormat) -> Unit,
    onRefresh: () -> Unit,
    onDismiss: () -> Unit
) {
    val palette: AppPalette =
        if (darkMode) {
            DarkPalette
        } else {
            LightPalette
        }

    AppDialog(
        palette = palette,
        onDismiss = onDismiss
    ) { dismiss ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DialogContentPadding),
            verticalArrangement = Arrangement.spacedBy(DialogSectionSpacing)
        ) {
            // HEADER
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 1.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Display & Preferences",
                        color = palette.primaryDark,
                        fontSize = scaledSp(16f, fontScale),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Adjust layout and visual settings",
                        color = palette.secondaryText,
                        fontSize = scaledSp(12f, fontScale),
                        fontWeight = FontWeight.Normal
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(DialogCloseSize)
                        .clickable {
                            dismiss()
                        },
                    shape = RoundedCornerShape(DialogActionRadius),
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = "Close",
                            tint = palette.secondaryText,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                color = palette.secondaryText.copy(
                    alpha = DialogDividerAlpha
                )
            )

            // TEXT SIZE
            SettingsSection(
                title = "TEXT SIZE",
                palette = palette,
                fontScale = fontScale
            ) {
                SettingsSegmentedContainer(
                    palette = palette
                ) {
                    SettingsSegmentButton(
                        text = "A-",
                        selected = false,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = true,
                        onClick = {
                            onFontScaleChange(
                                (fontScale - 0.1f)
                                    .coerceAtLeast(0.8f)
                            )
                        }
                    )

                    SettingsSegmentButton(
                        text = "Reset",
                        selected = fontScale == 1f,
                        icon = Icons.Outlined.RestartAlt,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = true,
                        onClick = {
                            onFontScaleChange(1f)
                        }
                    )

                    SettingsSegmentButton(
                        text = "A+",
                        selected = false,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = false,
                        onClick = {
                            onFontScaleChange(
                                (fontScale + 0.1f)
                                    .coerceAtMost(1.4f)
                            )
                        }
                    )
                }
            }

            // APPEARANCE
            SettingsSection(
                title = "APPEARANCE",
                palette = palette,
                fontScale = fontScale
            ) {
                SettingsSegmentedContainer(
                    palette = palette
                ) {
                    SettingsSegmentButton(
                        text = "Light",
                        selected = themeMode == AppThemeMode.LIGHT,
                        icon = Icons.Outlined.LightMode,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = true,
                        onClick = {
                            onThemeModeChange(
                                AppThemeMode.LIGHT
                            )
                        }
                    )

                    SettingsSegmentButton(
                        text = "System",
                        selected = themeMode == AppThemeMode.SYSTEM,
                        icon = Icons.Outlined.SettingsSuggest,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = true,
                        onClick = {
                            onThemeModeChange(
                                AppThemeMode.SYSTEM
                            )
                        }
                    )

                    SettingsSegmentButton(
                        text = "Dark",
                        selected = themeMode == AppThemeMode.DARK,
                        icon = Icons.Outlined.DarkMode,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = false,
                        onClick = {
                            onThemeModeChange(
                                AppThemeMode.DARK
                            )
                        }
                    )
                }
            }

            // TIME FORMAT
            SettingsSection(
                title = "TIME FORMAT",
                palette = palette,
                fontScale = fontScale
            ) {
                SettingsSegmentedContainer(
                    palette = palette
                ) {
                    SettingsSegmentButton(
                        text = "12-Hour",
                        selected =
                            timeFormat == AppTimeFormat.TWELVE_HOUR,
                        icon = Icons.Outlined.Schedule,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = true,
                        onClick = {
                            onTimeFormatChange(
                                AppTimeFormat.TWELVE_HOUR
                            )
                        }
                    )

                    SettingsSegmentButton(
                        text = "24-Hour",
                        selected =
                            timeFormat == AppTimeFormat.TWENTY_FOUR_HOUR,
                        icon = Icons.Outlined.AccessTime,
                        palette = palette,
                        fontScale = fontScale,
                        showEndDivider = false,
                        onClick = {
                            onTimeFormatChange(
                                AppTimeFormat.TWENTY_FOUR_HOUR
                            )
                        }
                    )
                }
            }

            // REFRESH
            SettingsActionButton(
                text = "Refresh messages",
                icon = Icons.Outlined.Refresh,
                palette = palette,
                fontScale = fontScale,
                onClick = onRefresh
            )

            // FOOTER DIVIDER
            HorizontalDivider(
                color = palette.secondaryText.copy(
                    alpha = DialogDividerAlpha
                )
            )

            // CANCEL / DONE
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingsFooterButton(
                    text = "Cancel",
                    primary = false,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = dismiss
                )

                SettingsFooterButton(
                    text = "Done",
                    primary = true,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = dismiss
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    title: String,
    palette: AppPalette,
    fontScale: Float,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(
            DialogSectionTitleSpacing
        )
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = 2.dp),
            color = palette.secondaryText,
            fontSize = scaledSp(11f, fontScale),
            fontWeight = FontWeight.Bold
        )

        content()
    }
}

@Composable
private fun SettingsSegmentedContainer(
    palette: AppPalette,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(DialogSegmentHeight),
        shape = RoundedCornerShape(DialogSegmentRadius),
        color = palette.controlBackground,
        border = BorderStroke(
            width = 1.dp,
            color = palette.primary.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            content = content
        )
    }
}

@Composable
private fun RowScope.SettingsSegmentButton(
    text: String,
    selected: Boolean,
    palette: AppPalette,
    fontScale: Float,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    showEndDivider: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .height(DialogSegmentHeight)
            .drawBehind {
                if (showEndDivider) {
                    drawLine(
                        color = palette.primary.copy(alpha = 0.20f),
                        start = androidx.compose.ui.geometry.Offset(
                            size.width - 0.5f,
                            0f
                        ),
                        end = androidx.compose.ui.geometry.Offset(
                            size.width - 0.5f,
                            size.height
                        ),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(0.dp),
        color = if (selected) {
            if (palette == DarkPalette) {
                palette.surface
            } else {
                Color.White
            }
        } else {
            Color.Transparent
        },
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (selected) {
                        palette.primary
                    } else {
                        palette.secondaryText
                    },
                    modifier = Modifier.size(16.dp)
                )

                Spacer(
                    modifier = Modifier.size(4.dp)
                )
            }

            Text(
                text = text,
                color = if (selected) {
                    palette.primary
                } else {
                    palette.secondaryText
                },
                fontSize = scaledSp(14f, fontScale),
                fontWeight = if (selected) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Medium
                }
            )
        }
    }
}

@Composable
private fun SettingsActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    palette: AppPalette,
    fontScale: Float,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(DialogActionHeight)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(DialogActionRadius),
        color = Color.Transparent,
        border = BorderStroke(
            1.dp,
            palette.secondaryText.copy(alpha = 0.25f)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = palette.primary,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )
            }

            Text(
                text = text,
                color = palette.primaryDark,
                fontSize = scaledSp(14f, fontScale),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun RowScope.SettingsFooterButton(
    text: String,
    primary: Boolean,
    palette: AppPalette,
    fontScale: Float,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(DialogActionHeight)
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(DialogActionRadius),
        color = if (primary) {
            palette.primary
        } else {
            Color.Transparent
        },
        border = if (!primary) {
            BorderStroke(
                1.dp,
                palette.secondaryText.copy(alpha = 0.25f)
            )
        } else {
            null
        },
        shadowElevation = if (primary) {
            1.dp
        } else {
            0.dp
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = if (primary) {
                    Color.White
                } else {
                    palette.primaryDark
                },
                fontSize = scaledSp(14f, fontScale),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}