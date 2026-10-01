package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppFontSearch
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.AppPillRadius
import com.nviswanathareddy.messagesexplorer.utils.AppScreenHorizontalPadding
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

@Composable
fun SearchSection(
    query: String,
    onQueryChange: (String) -> Unit,
    palette: AppPalette,
    fontScale: Float,
    onClose: () -> Unit,
) {
  Surface(
      modifier =
          Modifier.fillMaxWidth()
              .padding(horizontal = AppScreenHorizontalPadding, vertical = 10.dp),
      shape = RoundedCornerShape(AppPillRadius),
      color = palette.controlBackground,
  ) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
      IconButton(onClick = onClose) {
        Icon(
            Icons.AutoMirrored.Outlined.ArrowBack,
            "Back",
            tint = palette.primaryDark,
        )
      }
      Icon(Icons.Outlined.Search, null, tint = palette.secondaryText)
      BasicTextField(
          value = query,
          onValueChange = onQueryChange,
          modifier = Modifier.weight(1f).padding(horizontal = 10.dp),
          singleLine = true,
          textStyle =
              TextStyle(
                  color = palette.primaryDark,
                  fontSize =
                      scaledSp(
                          AppFontSearch,
                          fontScale,
                      ),
              ),
          cursorBrush = SolidColor(palette.primary),
          decorationBox = { inner ->
            if (query.isEmpty())
                Text(
                    "Search messages",
                    color = palette.secondaryText,
                    fontSize =
                        scaledSp(
                            AppFontSearch,
                            fontScale,
                        ),
                )
            inner()
          },
      )
      if (query.isNotEmpty())
          IconButton(onClick = { onQueryChange("") }) {
            Icon(
                Icons.Outlined.Close,
                "Clear",
                tint = palette.secondaryText,
            )
          }
    }
  }
}
