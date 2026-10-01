package com.nviswanathareddy.messagesexplorer.utils

import java.util.Locale

data class DomainResult(
    val domain: String?,
    val confidence: Int,
)

fun resolveDomain(
    senderInfo: SenderInfo,
    body: String,
): DomainResult {
  val sender = senderInfo.displayName.lowercase(Locale.getDefault()).replace(Regex("[^a-z0-9]"), "")
  val text = "$sender ${body.lowercase(Locale.getDefault())}"
  val brand = extractBrand(text)
  if (brand.isBlank()) {
    return DomainResult(
        domain = null,
        confidence = 0,
    )
  }
  return DomainResult(
      domain = "$brand.com",
      confidence = 50,
  )
}

private fun extractBrand(text: String): String {
  val words =
      text
          .split(
              Regex("[^a-z0-9]+"),
              limit = 100,
          )
          .filter {
            it.length >= 4
          }
  for (word in words) {
    if (isLikelyBrand(word)) {
      return word
    }
  }
  return ""
}

private fun isLikelyBrand(word: String): Boolean {
  if (word.length < 4) return false
  if (word.all { it.isDigit() }) return false
  if (word in commonSmsWords) return false
  return true
}

private val commonSmsWords =
    setOf(
        "your",
        "from",
        "with",
        "have",
        "been",
        "this",
        "that",
        "will",
        "your",
        "account",
        "amount",
        "balance",
        "transaction",
        "payment",
        "credited",
        "debited",
        "debit",
        "credit",
        "received",
        "paid",
        "payment",
        "order",
        "delivery",
        "available",
        "please",
        "click",
        "verification",
        "password",
        "message",
        "offer",
        "alert",
        "bank",
        "card",
        "code",
    )
