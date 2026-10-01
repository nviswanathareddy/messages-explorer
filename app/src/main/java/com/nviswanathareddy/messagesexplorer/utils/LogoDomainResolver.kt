package com.nviswanathareddy.messagesexplorer.utils

import java.util.Locale

fun resolveLogoDomain(
    senderInfo: SenderInfo,
    body: String,
): String? {
  val sender = normalizeForLookup(senderInfo.key)
  val message = normalizeForLookup(body)
  val combined = "$sender $message"
  return when {
    containsAny(combined, "sbi", "statebankofindia") -> "sbi.co.in"
    containsAny(combined, "hdfc") -> "hdfcbank.com"
    containsAny(combined, "icici") -> "icicibank.com"
    containsAny(combined, "axisbank", "axis") -> "axisbank.com"
    containsAny(combined, "canara", "canbnk") -> "canarabank.com"
    containsAny(combined, "federalbank", "federal") -> "federalbank.co.in"
    containsAny(combined, "kotak") -> "kotak.com"
    containsAny(combined, "indusind") -> "indusind.com"
    containsAny(combined, "idfcfirst", "idfc") -> "idfcfirstbank.com"
    containsAny(combined, "yesbank") -> "yesbank.in"
    containsAny(combined, "amazon") -> "amazon.in"
    containsAny(combined, "flipkart") -> "flipkart.com"
    containsAny(combined, "meesho") -> "meesho.com"
    containsAny(combined, "cred") -> "cred.club"
    containsAny(combined, "medplus") -> "medplusmart.com"
    else -> resolvePossibleDomain(senderInfo, body)
  }
}

private fun resolvePossibleDomain(
    senderInfo: SenderInfo,
    body: String,
): String? {
  val sender = normalizeForLookup(senderInfo.displayName)
  if (sender.length < 4) {
    return null
  }
  if (
      containsAny(
          sender,
          "otp",
          "alert",
          "info",
          "bank",
          "pay",
          "service",
          "support",
          "verify",
          "secure",
          "update",
      )
  ) {
    return null
  }
  val possibleBrand = sender.replace(Regex("[^a-z0-9]"), "").lowercase(Locale.getDefault())
  if (possibleBrand.length < 4) {
    return null
  }
  return "$possibleBrand.com"
}

private fun normalizeForLookup(value: String): String {
  return value.lowercase(Locale.getDefault()).replace(Regex("[^a-z0-9]"), "")
}

private fun containsAny(
    text: String,
    vararg values: String,
): Boolean {
  return values.any { value ->
    text.contains(value)
  }
}
