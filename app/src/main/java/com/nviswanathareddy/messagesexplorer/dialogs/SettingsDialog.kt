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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.AppThemeMode
import com.nviswanathareddy.messagesexplorer.AppTimeFormat
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

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
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
            verticalArrangement =
                Arrangement.spacedBy(16.dp)
        ) {

            // -----------------------------------------------------
            // HEADER
            // -----------------------------------------------------

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
                        text = "Display & Preferences",
                        color = palette.primaryDark,
                        fontSize =
                            scaledSp(
                                16f,
                                fontScale
                            ),
                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text = "Adjust layout and visual settings",
                        color = palette.secondaryText,
                        fontSize =
                            scaledSp(
                                12f,
                                fontScale
                            ),
                        fontWeight =
                            FontWeight.Normal
                    )
                }

                Surface(
                    modifier =
                        Modifier
                            .size(32.dp)
                            .clickable {
                                dismiss()
                            },
                    shape =
                        RoundedCornerShape(
                            16.dp
                        ),
                    color =
                        Color.Transparent
                ) {
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.Center,
                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector =
                                Icons.Outlined.Close,
                            contentDescription =
                                "Close",
                            tint =
                                palette.secondaryText,
                            modifier =
                                Modifier.size(20.dp)
                        )
                    }
                }
            }

            // -----------------------------------------------------
            // TEXT SIZE
            // -----------------------------------------------------

            SettingsSectionTitle(
                title = "TEXT SIZE",
                palette = palette,
                fontScale = fontScale
            )

            SettingsSegmentedContainer(
                palette = palette
            ) {

                SettingsSegmentButton(
                    text = "A-",
                    selected = false,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onFontScaleChange(
                            (fontScale - 0.1f)
                                .coerceAtLeast(0.8f)
                        )
                    }
                )

                SettingsSegmentButton(
                    text = "Reset",
                    selected =
                        fontScale == 1f,
                    icon =
                        Icons.Outlined.RestartAlt,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onFontScaleChange(1f)
                    }
                )

                SettingsSegmentButton(
                    text = "A+",
                    selected = false,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onFontScaleChange(
                            (fontScale + 0.1f)
                                .coerceAtMost(1.4f)
                        )
                    }
                )
            }

            // -----------------------------------------------------
            // APPEARANCE
            // -----------------------------------------------------

            SettingsSectionTitle(
                title = "APPEARANCE",
                palette = palette,
                fontScale = fontScale
            )

            SettingsSegmentedContainer(
                palette = palette
            ) {

                SettingsSegmentButton(
                    text = "Light",
                    selected =
                        themeMode ==
                                AppThemeMode.LIGHT,
                    icon =
                        Icons.Outlined.LightMode,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onThemeModeChange(
                            AppThemeMode.LIGHT
                        )
                    }
                )

                SettingsSegmentButton(
                    text = "Dark",
                    selected =
                        themeMode ==
                                AppThemeMode.DARK,
                    icon =
                        Icons.Outlined.DarkMode,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onThemeModeChange(
                            AppThemeMode.DARK
                        )
                    }
                )

                SettingsSegmentButton(
                    text = "System",
                    selected =
                        themeMode ==
                                AppThemeMode.SYSTEM,
                    icon =
                        Icons.Outlined.SettingsSuggest,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onThemeModeChange(
                            AppThemeMode.SYSTEM
                        )
                    }
                )
            }

            // -----------------------------------------------------
            // TIME FORMAT
            // -----------------------------------------------------

            SettingsSectionTitle(
                title = "TIME FORMAT",
                palette = palette,
                fontScale = fontScale
            )

            SettingsSegmentedContainer(
                palette = palette
            ) {

                SettingsSegmentButton(
                    text = "12-Hour",
                    selected =
                        timeFormat ==
                                AppTimeFormat.TWELVE_HOUR,
                    icon =
                        Icons.Outlined.Schedule,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onTimeFormatChange(
                            AppTimeFormat.TWELVE_HOUR
                        )
                    }
                )

                SettingsSegmentButton(
                    text = "24-Hour",
                    selected =
                        timeFormat ==
                                AppTimeFormat.TWENTY_FOUR_HOUR,
                    icon =
                        Icons.Outlined.AccessTime,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = {
                        onTimeFormatChange(
                            AppTimeFormat.TWENTY_FOUR_HOUR
                        )
                    }
                )
            }

            // -----------------------------------------------------
            // ACTIONS
            // -----------------------------------------------------

            Column(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                SettingsActionButton(
                    text = "Refresh messages",
                    icon =
                        Icons.Outlined.Refresh,
                    primary = false,
                    palette = palette,
                    fontScale = fontScale,
                    onClick = onRefresh
                )

                SettingsActionButton(
                    text = "Done",
                    icon = null,
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
private fun SettingsSectionTitle(
    title: String,
    palette: AppPalette,
    fontScale: Float
) {
    Text(
        text = title,
        modifier =
            Modifier.padding(
                horizontal = 2.dp
            ),
        color = palette.secondaryText,
        fontSize =
            scaledSp(
                11f,
                fontScale
            ),
        fontWeight =
            FontWeight.Bold
    )
}

@Composable
private fun SettingsSegmentedContainer(
    palette: AppPalette,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(40.dp),
        shape =
            RoundedCornerShape(
                12.dp
            ),
        color =
            palette.controlBackground
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
            horizontalArrangement =
                Arrangement.spacedBy(2.dp),
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
    onClick: () -> Unit
) {
    Surface(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .height(36.dp)
                .clickable {
                    onClick()
                },
        shape =
            RoundedCornerShape(
                8.dp
            ),
        color =
            if (selected) {
                if (palette == DarkPalette) {
                    palette.surface
                } else {
                    Color.White
                }
            } else {
                Color.Transparent
            },
        shadowElevation =
            if (selected) {
                1.dp
            } else {
                0.dp
            }
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.Center,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            if (icon != null) {
                Icon(
                    imageVector =
                        icon,
                    contentDescription =
                        null,
                    tint =
                        if (selected) {
                            palette.primary
                        } else {
                            palette.secondaryText
                        },
                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.size(4.dp)
                )
            }

            Text(
                text = text,
                color =
                    if (selected) {
                        palette.primary
                    } else {
                        palette.secondaryText
                    },
                fontSize =
                    scaledSp(
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
        }
    }
}

@Composable
private fun SettingsActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?,
    primary: Boolean,
    palette: AppPalette,
    fontScale: Float,
    onClick: () -> Unit
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable {
                    onClick()
                },
        shape =
            RoundedCornerShape(
                12.dp
            ),
        color =
            if (primary) {
                palette.primary
            } else {
                Color.Transparent
            },
        border =
            if (!primary) {
                BorderStroke(
                    1.dp,
                    palette.secondaryText.copy(
                        alpha = 0.25f
                    )
                )
            } else {
                null
            },
        shadowElevation =
            if (primary) {
                1.dp
            } else {
                0.dp
            }
    ) {
        Row(
            modifier =
                Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.Center,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            if (icon != null) {
                Icon(
                    imageVector =
                        icon,
                    contentDescription =
                        null,
                    tint =
                        if (primary) {
                            Color.White
                        } else {
                            palette.primary
                        },
                    modifier =
                        Modifier.size(18.dp)
                )

                Spacer(
                    modifier =
                        Modifier.size(8.dp)
                )
            }

            Text(
                text = text,
                color =
                    if (primary) {
                        Color.White
                    } else {
                        palette.primaryDark
                    },
                fontSize =
                    scaledSp(
                        14f,
                        fontScale
                    ),
                fontWeight =
                    if (primary) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Medium
                    }
            )
        }
    }
}