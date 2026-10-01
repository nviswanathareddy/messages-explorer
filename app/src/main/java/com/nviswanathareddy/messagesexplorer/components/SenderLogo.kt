package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
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
import com.nviswanathareddy.messagesexplorer.data.LogoRepository
import com.nviswanathareddy.messagesexplorer.utils.LogoConfig
import com.nviswanathareddy.messagesexplorer.utils.SenderInfo
import com.nviswanathareddy.messagesexplorer.utils.buildLogoNameUrl
import com.nviswanathareddy.messagesexplorer.utils.buildLogoUrl
import com.nviswanathareddy.messagesexplorer.utils.rememberLogoResolution

private val DefaultSenderBackground = Color(0xFFE8F1FF)
private val DefaultSenderIconColor = Color(0xFF4A90E2)

@Composable
fun SenderLogo(
    senderInfo: SenderInfo,
    body: String,
    logoRepository: LogoRepository,
    modifier: Modifier = Modifier,
) {
  val resolution =
      rememberLogoResolution(
          senderInfo = senderInfo,
          body = body,
          logoRepository = logoRepository,
      )

  val logoUrl =
      remember(
          resolution.domain,
          resolution.brandName,
      ) {
        when {
          !resolution.domain.isNullOrBlank() -> {
            buildLogoUrl(
                domain = resolution.domain,
                logoApiToken = LogoConfig.LOGO_API_TOKEN,
            )
          }

          !resolution.brandName.isNullOrBlank() -> {
            buildLogoNameUrl(
                name = resolution.brandName,
                logoApiToken = LogoConfig.LOGO_API_TOKEN,
            )
          }

          else -> {
            null
          }
        }
      }

  var imageLoaded by
      remember(logoUrl) {
        mutableStateOf(false)
      }

  Box(
      modifier = modifier.size(40.dp).clip(CircleShape).background(DefaultSenderBackground),
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
      DefaultSenderIcon(
          senderInfo = senderInfo,
      )
    }
  }
}

@Composable
private fun DefaultSenderIcon(
    senderInfo: SenderInfo,
) {
  Icon(
      imageVector = Icons.Outlined.Person,
      contentDescription = senderInfo.displayName,
      tint = DefaultSenderIconColor,
      modifier = Modifier.size(22.dp),
  )
}
