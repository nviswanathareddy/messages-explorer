package com.nviswanathareddy.messagesexplorer.utils

fun buildLogoUrl(
  domain: String,
  logoApiToken: String
): String {
  return "https://img.logo.dev/$domain?token=$logoApiToken"
}