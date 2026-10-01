package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    onSortClick: () -> Unit,
) {
  Surface(
      modifier = Modifier.fillMaxWidth().height(40.dp),
      shape = RoundedCornerShape(8.dp),
      color = palette.controlBackground,
      border =
          BorderStroke(
              1.dp,
              palette.secondaryText.copy(alpha = 0.12f),
          ),
  ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      Surface(
          modifier =
              Modifier.weight(1f).height(40.dp).clickable {
                onCenterClick()
              },
          color = Color.Transparent,
      ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
              imageVector = Icons.Outlined.CalendarToday,
              contentDescription = "Select date or month",
              modifier = Modifier.size(16.dp),
              tint = palette.primary,
          )
          Text(
              text = centerText,
              modifier = Modifier.padding(start = 5.dp),
              maxLines = 1,
              color = palette.primary,
              fontSize = scaledSp(12f, fontScale),
              fontWeight = FontWeight.SemiBold,
          )
        }
      }
      Surface(
          modifier = Modifier.height(40.dp).weight(1f),
          color = palette.primary,
          border =
              BorderStroke(
                  1.dp,
                  palette.secondaryText.copy(alpha = 0.12f),
              ),
      ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
              text = messageCount.toString(),
              color = Color.White,
              fontSize = scaledSp(16f, fontScale),
              fontWeight = FontWeight.Bold,
          )
        }
      }
      Surface(
          modifier =
              Modifier.weight(1f).height(40.dp).clickable {
                onSortClick()
              },
          color = Color.Transparent,
      ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
          Icon(
              imageVector = Icons.Outlined.SwapVert,
              contentDescription = "Sort messages",
              modifier = Modifier.size(16.dp),
              tint = palette.primary,
          )
          Text(
              text =
                  when (sortOption) {
                    MessageSort.NEWEST_FIRST -> "Newest first"
                    MessageSort.OLDEST_FIRST -> "Oldest first"
                    MessageSort.SENDER_A_TO_Z -> "Sender A-Z"
                  },
              modifier = Modifier.padding(start = 5.dp),
              maxLines = 1,
              color = palette.primary,
              fontSize = scaledSp(12f, fontScale),
              fontWeight = FontWeight.SemiBold,
          )
        }
      }
    }
  }
}
