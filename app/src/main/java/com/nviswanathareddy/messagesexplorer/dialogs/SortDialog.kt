package com.nviswanathareddy.messagesexplorer.dialogs

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.SortByAlpha
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.model.MessageSort
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

private val DialogContentPadding = 20.dp
private val DialogHeaderBottomPadding = 12.dp
private val DialogOptionPadding = 14.dp
private val DialogOptionRadius = 12.dp
private val DialogOptionSpacing = 6.dp
private val DialogFooterTopPadding = 8.dp
private val DialogButtonHeight = 48.dp
private val DialogButtonRadius = 12.dp
private val DialogCloseButtonSize = 36.dp
private val DialogOptionIconSize = 20.dp
private val DialogCheckSize = 24.dp

@Composable
fun SortDialog(
    sortOption: MessageSort,
    fontScale: Float,
    darkMode: Boolean,
    onDismiss: () -> Unit,
    onApply: (MessageSort) -> Unit
) {
    val palette: AppPalette =
        if (darkMode) {
            DarkPalette
        } else {
            LightPalette
        }

    var selectedSort by remember {
        mutableStateOf(sortOption)
    }

    AppDialog(
        palette = palette,
        onDismiss = onDismiss
    ) { dismiss ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    DialogContentPadding
                )
        ) {

            // -----------------------------------------------------
            // HEADER
            // -----------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom =
                            DialogHeaderBottomPadding
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Column(
                    modifier =
                        Modifier.weight(1f)
                ) {
                    Text(
                        text = "Sort Messages",
                        color =
                            palette.primaryDark,
                        fontSize =
                            scaledSp(
                                16f,
                                fontScale
                            ),
                        fontWeight =
                            FontWeight.Bold
                    )

                    Text(
                        text =
                            "Choose order to display your inbox",
                        color =
                            palette.secondaryText,
                        fontSize =
                            scaledSp(
                                12f,
                                fontScale
                            ),
                        fontWeight =
                            FontWeight.Normal
                    )
                }

                IconButton(
                    onClick = dismiss,
                    modifier =
                        Modifier.size(
                            DialogCloseButtonSize
                        )
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

            HorizontalDivider(
                color =
                    palette.secondaryText.copy(
                        alpha = 0.18f
                    )
            )

            // -----------------------------------------------------
            // OPTIONS
            // -----------------------------------------------------

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 12.dp
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(
                        DialogOptionSpacing
                    )
            ) {

                SortOption(
                    title = "Newest first",
                    selected =
                        selectedSort ==
                                MessageSort.NEWEST_FIRST,
                    palette = palette,
                    fontScale = fontScale,
                    icon =
                        Icons.Outlined.ArrowDownward,
                    onClick = {
                        selectedSort =
                            MessageSort.NEWEST_FIRST
                    }
                )

                SortOption(
                    title = "Oldest first",
                    selected =
                        selectedSort ==
                                MessageSort.OLDEST_FIRST,
                    palette = palette,
                    fontScale = fontScale,
                    icon =
                        Icons.Outlined.ArrowUpward,
                    onClick = {
                        selectedSort =
                            MessageSort.OLDEST_FIRST
                    }
                )

                SortOption(
                    title = "A - Z (Alphabetical)",
                    selected =
                        selectedSort ==
                                MessageSort.SENDER_A_TO_Z,
                    palette = palette,
                    fontScale = fontScale,
                    icon =
                        Icons.Outlined.SortByAlpha,
                    onClick = {
                        selectedSort =
                            MessageSort.SENDER_A_TO_Z
                    }
                )
            }

            // -----------------------------------------------------
            // ACTION FOOTER
            // -----------------------------------------------------

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top =
                            DialogFooterTopPadding
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp
                    )
            ) {

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(
                            DialogButtonHeight
                        )
                        .border(
                            width = 1.dp,
                            color =
                                palette.secondaryText.copy(
                                    alpha = 0.20f
                                ),
                            shape =
                                RoundedCornerShape(
                                    DialogButtonRadius
                                )
                        )
                        .clickable {
                            dismiss()
                        },
                    shape =
                        RoundedCornerShape(
                            DialogButtonRadius
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
                            text = "Cancel",
                            color =
                                palette.secondaryText,
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

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .height(
                            DialogButtonHeight
                        )
                        .clickable {
                            onApply(
                                selectedSort
                            )
                            dismiss()
                        },
                    shape =
                        RoundedCornerShape(
                            DialogButtonRadius
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
                            text = "Apply",
                            color = Color.White,
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
private fun SortOption(
    title: String,
    selected: Boolean,
    palette: AppPalette,
    fontScale: Float,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    val background =
        if (selected) {
            palette.controlBackground
        } else {
            Color.Transparent
        }

    val borderColor =
        if (selected) {
            palette.primary.copy(
                alpha = 0.20f
            )
        } else {
            Color.Transparent
        }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = borderColor,
                shape =
                    RoundedCornerShape(
                        DialogOptionRadius
                    )
            )
            .clickable {
                onClick()
            },
        shape =
            RoundedCornerShape(
                DialogOptionRadius
            ),
        color = background
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    DialogOptionPadding
                ),
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Row(
                modifier =
                    Modifier.weight(1f),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint =
                        if (selected) {
                            palette.primary
                        } else {
                            palette.secondaryText
                        },
                    modifier =
                        Modifier.size(
                            DialogOptionIconSize
                        )
                )

                Spacer(
                    modifier =
                        Modifier.width(12.dp)
                )

                Text(
                    text = title,
                    color =
                        if (selected) {
                            palette.primaryDark
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

            if (selected) {
                Surface(
                    modifier =
                        Modifier.size(
                            DialogCheckSize
                        ),
                    shape =
                        RoundedCornerShape(
                            50
                        ),
                    color =
                        palette.primary
                ) {
                    Icon(
                        imageVector =
                            Icons.Outlined.Check,
                        contentDescription =
                            "Selected",
                        tint = Color.White,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                    )
                }
            } else {
                Surface(
                    modifier =
                        Modifier.size(
                            DialogCheckSize
                        ),
                    shape =
                        RoundedCornerShape(
                            50
                        ),
                    color = Color.Transparent
                ) {
                    Surface(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                        shape =
                            RoundedCornerShape(
                                50
                            ),
                        color = Color.Transparent
                    ) {
                        BoxWithBorder(
                            palette = palette
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxWithBorder(
    palette: AppPalette
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color =
                    palette.secondaryText.copy(
                        alpha = 0.35f
                    ),
                shape =
                    RoundedCornerShape(
                        50
                    )
            ),
        shape =
            RoundedCornerShape(
                50
            ),
        color = Color.Transparent
    ) {}
}