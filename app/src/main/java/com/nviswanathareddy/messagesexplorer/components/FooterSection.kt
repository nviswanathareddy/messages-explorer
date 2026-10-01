package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

enum class FooterTab {
  MESSAGES,
  CALENDAR,
}

@Composable
fun FooterSection(
    selectedTab: FooterTab,
    onTabSelected: (FooterTab) -> Unit,
    palette: AppPalette,
    fontScale: Float,
) {
  Surface(
      modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
      color = Color.White,
      tonalElevation = 0.dp,
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      FooterItem(
          modifier = Modifier.weight(1f),
          icon = Icons.AutoMirrored.Outlined.Chat,
          label = "Messages",
          selected = selectedTab == FooterTab.MESSAGES,
          onClick = {
            onTabSelected(FooterTab.MESSAGES)
          },
          palette = palette,
          fontScale = fontScale,
      )
      FooterItem(
          modifier = Modifier.weight(1f),
          icon = Icons.Outlined.CalendarToday,
          label = "Calendar",
          selected = selectedTab == FooterTab.CALENDAR,
          onClick = {
            onTabSelected(FooterTab.CALENDAR)
          },
          palette = palette,
          fontScale = fontScale,
      )
    }
  }
}

@Composable
private fun FooterItem(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    palette: AppPalette,
    fontScale: Float,
) {
  val backgroundColor =
      if (selected) {
        Color(0xFFDBE8FD)
      } else {
        Color.Transparent
      }
  val contentColor =
      if (selected) {
        Color(0xFF004AC6)
      } else {
        palette.primaryDark
      }
  Surface(
      modifier = modifier.height(56.dp).clickable(onClick = onClick),
      color = backgroundColor,
      tonalElevation = 0.dp,
  ) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
      Icon(
          imageVector = icon,
          contentDescription = label,
          tint = contentColor,
          modifier = Modifier.height(21.dp),
      )
      Text(
          text = label,
          color = contentColor,
          fontSize = scaledSp(12f, fontScale),
          fontWeight =
              if (selected) {
                FontWeight.SemiBold
              } else {
                FontWeight.Medium
              },
      )
    }
  }
}
