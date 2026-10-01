package com.nviswanathareddy.messagesexplorer.utils
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
@Composable
fun rememberLogoDomain(
    senderInfo: SenderInfo,
    body: String
): String? {
    return remember(
        senderInfo.key,
        body
    ) {
        resolveLogoDomain(
            senderInfo = senderInfo,
            body = body
        )
    }
}