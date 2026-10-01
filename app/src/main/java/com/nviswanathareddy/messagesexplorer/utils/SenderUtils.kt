package com.nviswanathareddy.messagesexplorer.utils

import java.util.Locale

data class SenderInfo(
    val key: String,
    val displayName: String,
)

fun resolveSenderInfo(
    sender: String,
    body: String = "",
): SenderInfo {
  val displayName =
      sender.trim().replace(Regex("^[A-Z]{2}-"), "").replace(Regex("-[A-Z]$"), "").ifBlank {
        sender.trim()
      }
  return SenderInfo(
      key = normalizeSender(sender),
      displayName = displayName,
  )
}

fun normalizeSender(sender: String): String {
  return sender
      .trim()
      .uppercase(Locale.getDefault())
      .replace(Regex("[^A-Z0-9]"), "")
      .lowercase(Locale.getDefault())
}
