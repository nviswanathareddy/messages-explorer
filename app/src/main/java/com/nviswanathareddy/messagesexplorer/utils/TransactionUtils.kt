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

  val hasCreditCard =
      containsAny(
          text,
          "credit card",
          "creditcard",
      )

  val hasDebitCard =
      containsAny(
          text,
          "debit card",
          "debitcard",
      )

  val hasCard =
      hasCreditCard ||
          hasDebitCard ||
          Regex(
                  """\bcard\s*(?:xx|x|\*|\()\s*\d{2,4}""",
              )
              .containsMatchIn(text)

  val label =
      when {
        hasUpi -> "UPI"
        hasCreditCard -> "CREDIT CARD"
        hasDebitCard -> "DEBIT CARD"
        hasCard -> "CARD"
        isAccountTransfer(text) -> "ACCOUNT"
        else -> ""
      }

  val reference =
      when {
        hasUpi && hasCard ->
            extractCardReference(text).ifBlank {
              extractAccountReference(text)
            }

        hasUpi -> extractAccountReference(text)

        hasCreditCard || hasDebitCard || hasCard -> extractCardReference(text)

        isAccountTransfer(text) -> extractAccountReference(text)

        else -> ""
      }

  /*
   * Do NOT reject a transaction just because a reference number
   * could not be extracted.
   *
   * Example:
   * "Rs 35,000.00 spent..."
   *
   * This is still a valid debit even if no card/account reference
   * can be extracted.
   */
  return TransactionInfo(
      type = type,
      label = label,
      reference = reference,
  )
}

private fun detectTransactionType(
    text: String,
): TransactionType {
  /*
   * Normal credit patterns:
   *
   * "Rs.100 credited"
   * "INR 500 credited"
   * "received Rs 500"
   * "deposit of Rs 500"
   */
  val creditAfterAmountPattern =
      Regex(
          """(?:credited|credit|received|deposit(?:ed)?)\b(?:\s+(?:with|by|of))?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?"""
      )

  /*
   * Some banks put the amount before the word:
   *
   * "INR 500 credited"
   * "Rs 500 received"
   */
  val creditBeforeAmountPattern =
      Regex(
          """(?:(?:rs\.?|inr|₹)\s*)[\d,]+(?:\.\d{1,2})?\s+(?:credited|credit|received|deposited|deposit)\b"""
      )

  /*
   * Normal debit patterns:
   *
   * "Rs 2,000 debited"
   * "Rs 500 paid"
   * "Rs 35,000 spent"
   */
  val debitAfterAmountPattern =
      Regex(
          """(?:debited|debit|spent|spend|paid|withdrawn|withdrawal)\b(?:\s+(?:by|of))?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?"""
      )

  /*
   * Important:
   *
   * ICICI/RBL examples:
   *
   * "Rs 35,000.00 spent"
   * "INR800.00 spent"
   *
   * Amount comes BEFORE "spent".
   */
  val debitBeforeAmountPattern =
      Regex(
          """(?:(?:rs\.?|inr|₹)\s*)[\d,]+(?:\.\d{1,2})?\s+(?:debited|debit|spent|spend|paid|withdrawn|withdrawal)\b"""
      )

  /*
   * Banking SMS frequently use:
   *
   * "Dr INR 1,10,000"
   * "Dr Rs 500"
   */
  val debitDrPattern = Regex("""\bdr\.?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?\b""")

  /*
   * Some banks use:
   *
   * "CR INR 500"
   * "Cr Rs 500"
   */
  val creditCrPattern = Regex("""\bcr\.?\s*(?:(?:rs\.?|inr|₹)\s*)?[\d,]+(?:\.\d{1,2})?\b""")

  return when {
    debitDrPattern.containsMatchIn(text) -> TransactionType.DEBIT

    debitAfterAmountPattern.containsMatchIn(text) -> TransactionType.DEBIT

    debitBeforeAmountPattern.containsMatchIn(text) -> TransactionType.DEBIT

    creditCrPattern.containsMatchIn(text) -> TransactionType.CREDIT

    creditAfterAmountPattern.containsMatchIn(text) -> TransactionType.CREDIT

    creditBeforeAmountPattern.containsMatchIn(text) -> TransactionType.CREDIT

    else -> TransactionType.NONE
  }
}

private fun isAccountTransfer(
    text: String,
): Boolean {
  return containsAny(
      text,
      "imps",
      "neft",
      "rtgs",
      "account transfer",
      "transfer to",
      "transfer from",
  ) ||
      Regex(
              """\b(?:a/c|acct|account|account no|ac no)\b""",
          )
          .containsMatchIn(text)
}

private fun extractCardReference(
    text: String,
): String {
  val patterns =
      listOf(
          Regex(
              """(?:credit\s*card|creditcard|debit\s*card|debitcard|card)\s*(?:no\.?\s*)?[xX*]{0,8}(\d{3,4})(?!\d)"""
          ),
          Regex("""\b[xX*]{1,8}(\d{3,4})(?!\d)\b"""),
          Regex("""\(\s*(\d{3,4})\s*\)"""),
      )

  for (pattern in patterns) {
    val match = pattern.find(text)

    if (match != null) {
      return match.groupValues[1]
    }
  }

  return ""
}

private fun extractAccountReference(
    text: String,
): String {
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
