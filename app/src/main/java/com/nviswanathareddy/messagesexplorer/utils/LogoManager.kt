package com.nviswanathareddy.messagesexplorer.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.nviswanathareddy.messagesexplorer.data.LogoRepository

data class LogoResolution(
    val domain: String?,
    val brandName: String?,
)

@Composable
fun rememberLogoResolution(
    senderInfo: SenderInfo,
    body: String,
    logoRepository: LogoRepository,
): LogoResolution {
    return remember(
        senderInfo.key,
        logoRepository,
    ) {
        logoRepository.getLogoResolution(
            senderInfo = senderInfo,
            body = body,
        )
    }
}