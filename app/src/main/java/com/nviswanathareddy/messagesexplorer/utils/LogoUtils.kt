package com.nviswanathareddy.messagesexplorer.utils

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun buildLogoUrl(
  domain: String,
  logoApiToken: String,
): String {
  return "https://img.logo.dev/$domain?token=$logoApiToken"
}

fun buildLogoNameUrl(
  name: String,
  logoApiToken: String,
): String? {
  val cleanedName =
    name
      .trim()
      .replace(Regex("\\s+"), " ")

  if (cleanedName.isBlank()) {
    return null
  }

  val encodedName =
    URLEncoder.encode(
      cleanedName,
      StandardCharsets.UTF_8.toString(),
    )

  return "https://img.logo.dev/name/$encodedName?token=$logoApiToken&fallback=404"
}