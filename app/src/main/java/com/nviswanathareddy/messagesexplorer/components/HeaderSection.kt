package com.nviswanathareddy.messagesexplorer.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppFontSubtitle
import com.nviswanathareddy.messagesexplorer.utils.AppFontTopTitle
import com.nviswanathareddy.messagesexplorer.utils.AppHeaderControlSize
import com.nviswanathareddy.messagesexplorer.utils.AppHeaderHorizontalPadding
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

private val HeaderTopDividerSpacing = 0.dp
private val HeaderHeight = 70.dp
private val HeaderDividerColorAlpha = 0.18f

@Composable
fun HeaderSection(
    palette: AppPalette,
    fontScale: Float,
    onSearchClick: () -> Unit,
    onSettingsClick: () -> Unit,
    subtitle: String = "Explorer",
) {
  Surface(
      modifier = Modifier.fillMaxWidth(),
      color = palette.background,
      tonalElevation = 0.dp,
  ) {
    Column(modifier = Modifier.fillMaxWidth()) {
      Spacer(modifier = Modifier.height(HeaderTopDividerSpacing))
      Row(
          modifier =
              Modifier.fillMaxWidth()
                  .height(HeaderHeight)
                  .padding(horizontal = AppHeaderHorizontalPadding),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
        ) {
          Text(
              text = "Messages",
              color = palette.primaryDark,
              fontSize = scaledSp(AppFontTopTitle, fontScale),
              fontWeight = FontWeight.Bold,
              maxLines = 1,
          )
          Spacer(modifier = Modifier.height(1.dp))
          Text(
              text = subtitle,
              color = palette.primary,
              fontSize = scaledSp(AppFontSubtitle, fontScale),
              fontWeight = FontWeight.SemiBold,
              maxLines = 1,
          )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
              modifier =
                  Modifier.size(AppHeaderControlSize).clickable {
                    onSearchClick()
                  },
              shape = CircleShape,
              color = Color.Transparent,
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                  imageVector = Icons.Outlined.Search,
                  contentDescription = "Search",
                  tint = palette.primaryDark,
                  modifier = Modifier.size(21.dp),
              )
            }
          }
          Spacer(modifier = Modifier.width(5.dp))
          Surface(
              modifier =
                  Modifier.size(AppHeaderControlSize).clickable {
                    onSettingsClick()
                  },
              shape = CircleShape,
              color = Color.Transparent,
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                  imageVector = Icons.Outlined.Settings,
                  contentDescription = "Settings",
                  tint = palette.primaryDark,
                  modifier = Modifier.size(21.dp),
              )
            }
          }
          Spacer(modifier = Modifier.width(6.dp))
          Surface(
              modifier = Modifier.size(38.dp),
              shape = CircleShape,
              color = palette.primary,
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                  text = "JS",
                  color = Color.White,
                  fontSize = scaledSp(11f, fontScale),
                  fontWeight = FontWeight.Bold,
              )
            }
          }
        }
      }
      HorizontalDivider(
          color =
              palette.secondaryText.copy(
                  alpha = HeaderDividerColorAlpha,
              ),
      )
    }
  }
}
