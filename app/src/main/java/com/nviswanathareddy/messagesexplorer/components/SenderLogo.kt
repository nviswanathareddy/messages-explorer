package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.nviswanathareddy.messagesexplorer.utils.LogoConfig
import com.nviswanathareddy.messagesexplorer.utils.SenderInfo
import com.nviswanathareddy.messagesexplorer.utils.buildLogoUrl
import com.nviswanathareddy.messagesexplorer.utils.resolveLogoDomain

@Composable
fun SenderLogo(
    senderInfo: SenderInfo,
    body: String,
    modifier: Modifier = Modifier,
) {
  val domain =
      remember(
          senderInfo.key,
          body,
      ) {
        resolveLogoDomain(
            senderInfo = senderInfo,
            body = body,
        )
      }
  val logoUrl =
      remember(domain) {
        domain?.let {
          buildLogoUrl(
              domain = it,
              logoApiToken = LogoConfig.LOGO_API_TOKEN,
          )
        }
      }
  var imageLoaded by
      remember(logoUrl) {
        mutableStateOf(false)
      }
  Box(
      modifier = modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF0F1F3)),
      contentAlignment = Alignment.Center,
  ) {
    if (logoUrl != null) {
      AsyncImage(
          model = logoUrl,
          contentDescription = senderInfo.displayName,
          modifier = Modifier.size(34.dp).clip(CircleShape),
          contentScale = ContentScale.Fit,
          onSuccess = {
            imageLoaded = true
          },
          onError = {
            imageLoaded = false
          },
      )
    }
    if (!imageLoaded) {
      DefaultSenderIcon(senderInfo = senderInfo)
    }
  }
}

@Composable
private fun DefaultSenderIcon(senderInfo: SenderInfo) {
  Icon(
      imageVector = Icons.Outlined.Business,
      contentDescription = senderInfo.displayName,
      tint = Color(0xFF6B7280),
      modifier = Modifier.size(21.dp),
  )
}
