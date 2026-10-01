package com.nviswanathareddy.messagesexplorer.utils

import java.util.Locale

enum class TransactionType {
  CREDIT,
  DEBIT,
  NONE,
}

data class TransactionInfo(
    val type: TransactionType,
    val label: String,
    val reference: String,
) {
  val shortType: String
    get() =
        when (type) {
          TransactionType.CREDIT -> "C"
          TransactionType.DEBIT -> "D"
          TransactionType.NONE -> ""
        }

  val displayText: String
    get() {
      if (type == TransactionType.NONE) {
        return ""
      }
      return listOf(
              shortType,
              label,
              reference,
          )
          .filter { it.isNotBlank() }
          .let { parts ->
            if (parts.size >= 3) {
              "${parts[0]} ${parts[1]} · ${parts[2]}"
            } else {
              parts.joinToString(" ")
            }
          }
    }
}

fun detectTransaction(
    sender: String,
    body: String,
): TransactionInfo {
  val text = "$sender $body".lowercase(Locale.getDefault())

  val type = detectTransactionType(text)

  if (type == TransactionType.NONE) {
    return TransactionInfo(
        type = TransactionType.NONE,
        label = "",
        reference = "",
    )
  }

  val hasUpi =
      containsAny(
          text,
          "upi",
          "upi/p2a",
          "upi/p2p",
      )

  val hasCard =
      containsAny(
          text,
          "credit card",
          "creditcard",
          "debit card",
          "debitcard",
          "card xx",
          "card x",
      )

  val label =
      when {
        hasUpi && hasCard -> "UPI"
        hasUpi -> "UPI"
        containsAny(
            text,
            "credit card",
            "creditcard",
        ) -> "CREDIT CARD"
        containsAny(
            text,
            "debit card",
            "debitcard",
        ) -> "DEBIT CARD"
        isAccountTransfer(text) -> "ACCOUNT"
        else -> ""
      }

  if (label.isBlank()) {
    return TransactionInfo(
        type = TransactionType.NONE,
        label = "",
        reference = "",
    )
  }

  val reference =
      when (label) {
        "UPI" -> {
          if (hasCard) {
            extractCardReference(text)
          } else {
            extractAccountReference(text)
          }
        }
        "CREDIT CARD",
        "DEBIT CARD" -> {
          extractCardReference(text)
        }
        "ACCOUNT" -> {
          extractAccountReference(text)
        }
        else -> ""
      }

  if (reference.isBlank()) {
    return TransactionInfo(
        type = TransactionType.NONE,
        label = "",
        reference = "",
    )
  }

  return TransactionInfo(
      type = type,
      label = label,
      reference = reference,
  )
}

private fun detectTransactionType(text: String): TransactionType {
  val creditPattern =
      Regex(
          """(?:credited|credit|received|deposit(?:ed)?)\b(?:\s+(?:with|by))?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?"""
      )
  val debitPattern =
      Regex(
          """(?:debited|debit|spent|spend|paid|withdrawn|withdrawal)\b(?:\s+(?:by|of))?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?"""
      )

  return when {
    creditPattern.containsMatchIn(text) -> TransactionType.CREDIT
    debitPattern.containsMatchIn(text) -> TransactionType.DEBIT
    else -> TransactionType.NONE
  }
}

private fun isAccountTransfer(text: String): Boolean {
  return containsAny(
      text,
      "imps",
      "neft",
      "rtgs",
      "account transfer",
      "transfer to",
      "transfer from",
  ) || Regex("""\b(?:a/c|acct|account|account no|ac no)\b""").containsMatchIn(text)
}

private fun extractCardReference(text: String): String {
  val patterns =
      listOf(
          Regex(
              """(?:credit\s*card|creditcard|debit\s*card|debitcard|card)\s*(?:no\.?\s*)?[xX*]{0,8}(\d{4})(?!\d)"""
          ),
          Regex("""\b[xX*]{1,8}(\d{4})(?!\d)\b"""),
      )

  for (pattern in patterns) {
    val match = pattern.find(text)
    if (match != null) {
      return match.groupValues[1]
    }
  }

  return ""
}

private fun extractAccountReference(text: String): String {
  val patterns =
      listOf(
          Regex("""(?:a/c|acct|account|account no|ac no)[\s.:#-]*(?:no\.?\s*)?([xX*]*\d{2,})"""),
          Regex("""\b([xX*]{1,8}\d{2,})\b"""),
      )

  for (pattern in patterns) {
    val match = pattern.find(text)
    if (match != null) {
      val value = match.groupValues[1]
      val normalized = value.uppercase(Locale.getDefault())

      if (normalized.length >= 5) {
        return normalized.takeLast(5)
      }
    }
  }

  return ""
}

private fun containsAny(
    text: String,
    vararg values: String,
): Boolean {
  return values.any { value ->
    text.contains(value)
  }
}
