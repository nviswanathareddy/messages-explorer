package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.model.MessageSort
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

@Composable
fun MessageFilterBar(
    messageCount: Int,
    centerText: String,
    palette: AppPalette,
    fontScale: Float,
    sortOption: MessageSort,
    onCenterClick: () -> Unit,
    onSortClick: () -> Unit
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 4.dp,
                    vertical = 4.dp
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
            Text(
                text = "ALL MESSAGES",
                color = palette.secondaryText,
                fontSize =
                    scaledSp(
                        12f,
                        fontScale
                    ),
                fontWeight =
                    FontWeight.SemiBold
            )

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Surface(
                shape =
                    RoundedCornerShape(50.dp),
                color =
                    palette.dateControlBackground
            ) {
                Text(
                    text = messageCount.toString(),
                    modifier =
                        Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 3.dp
                        ),
                    color =
                        palette.primaryDark,
                    fontSize =
                        scaledSp(
                            11f,
                            fontScale
                        ),
                    fontWeight =
                        FontWeight.Bold
                )
            }
        }

        Surface(
            modifier =
                Modifier.clickable {
                    onCenterClick()
                },
            shape =
                RoundedCornerShape(9.dp),
            color =
                palette.dateControlBackground
        ) {
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 6.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector =
                        Icons.Outlined.CalendarToday,
                    contentDescription =
                        null,
                    tint =
                        palette.primary,
                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(6.dp)
                )

                Text(
                    text = centerText,
                    color =
                        palette.primary,
                    fontSize =
                        scaledSp(
                            12f,
                            fontScale
                        ),
                    fontWeight =
                        FontWeight.SemiBold
                )
            }
        }

        Surface(
            modifier =
                Modifier.clickable {
                    onSortClick()
                },
            color =
                Color.Transparent
        ) {
            Row(
                modifier =
                    Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 6.dp
                    ),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {
                Icon(
                    imageVector =
                        Icons.Outlined.SwapVert,
                    contentDescription =
                        "Sort",
                    tint =
                        palette.primary,
                    modifier =
                        Modifier.size(16.dp)
                )

                Spacer(
                    modifier =
                        Modifier.width(4.dp)
                )

                Text(
                    text =
                        when (sortOption) {
                            MessageSort.NEWEST_FIRST ->
                                "Newest first"

                            MessageSort.OLDEST_FIRST ->
                                "Oldest first"

                            MessageSort.SENDER_A_TO_Z ->
                                "Sender A-Z"
                        },
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
}