package com.nviswanathareddy.messagesexplorer.utils

import android.net.Uri
import java.util.Locale

data class DomainEvidence(
    val domain: String,
    val score: Int,
)

data class BrandEvidence(
    val name: String,
    val score: Int,
)

fun resolveLogoDomain(
    senderInfo: SenderInfo,
    body: String,
): String? {
  return extractDomainEvidence(body).maxByOrNull { it.score }?.domain
}

fun extractDomainEvidence(
    body: String,
): List<DomainEvidence> {
  if (body.isBlank()) {
    return emptyList()
  }

  val results = mutableMapOf<String, Int>()

  extractUrls(body).forEach { url ->
    normalizeDomain(url)?.let { domain ->
      results[domain] = (results[domain] ?: 0) + 10
    }
  }

  extractEmailDomains(body).forEach { domain ->
    results[domain] = (results[domain] ?: 0) + 8
  }

  extractPlainDomains(body).forEach { domain ->
    results[domain] = (results[domain] ?: 0) + 5
  }

  return results.map { (domain, score) ->
    DomainEvidence(
        domain = domain,
        score = score,
    )
  }
}

fun extractBrandEvidence(
    sender: String,
    body: String,
): List<BrandEvidence> {
  val results = mutableMapOf<String, Int>()

  extractSenderBrandCandidates(sender).forEach { candidate ->
    results[candidate] = (results[candidate] ?: 0) + 10
  }

  extractBodyBrandCandidates(body).forEach { candidate ->
    results[candidate] = (results[candidate] ?: 0) + 3
  }

  return results
      .map { (name, score) ->
        BrandEvidence(
            name = name,
            score = score,
        )
      }
      .sortedByDescending { it.score }
}

private fun extractSenderBrandCandidates(
    sender: String,
): List<String> {
  if (sender.isBlank()) {
    return emptyList()
  }

  val candidates = mutableSetOf<String>()

  val parts = sender.trim().split("-").map { it.trim() }.filter { it.isNotBlank() }

  parts.forEach { part ->
    val cleaned = part.replace(Regex("[^A-Za-z0-9]"), "").trim()

    if (cleaned.length >= 3) {
      candidates.add(cleaned)
    }

    /*
     * Some Indian sender IDs contain a brand name followed by
     * a service name.
     *
     * Example:
     * JIONET
     * JioWiFi
     *
     * Generate useful prefixes without maintaining a company list.
     */
    val normalized = cleaned.uppercase(Locale.getDefault())

    if (normalized.length >= 4) {
      for (length in 3..normalized.length) {
        val prefix = normalized.substring(0, length)

        if (isPotentialBrandToken(prefix)) {
          candidates.add(prefix)
        }
      }
    }
  }

  return candidates.sortedByDescending { it.length }
}

private fun extractBodyBrandCandidates(
    body: String,
): List<String> {
  if (body.isBlank()) {
    return emptyList()
  }

  val results = mutableSetOf<String>()

  /*
   * Extract normal Latin words from the message.
   *
   * This deliberately ignores Telugu/Hindi/etc. words for brand
   * discovery because Logo.dev company search expects a company
   * name/domain and arbitrary regional-language words can create
   * false matches.
   */
  val words =
      Regex("""\b[A-Za-z][A-Za-z0-9&.'-]{2,}\b""")
          .findAll(body)
          .map { match ->
            match.value
          }
          .toList()

  words.forEach { word ->
    val cleaned = word.trim('.', ',', ':', ';', '!', '?', '(', ')', '[', ']', '{', '}').trim()

    if (
        cleaned.length >= 3 &&
            cleaned.length <= 40 &&
            !looksLikeDomain(cleaned) &&
            !looksLikeUrl(cleaned) &&
            !looksLikeRandomCode(cleaned)
    ) {
      results.add(cleaned)
    }
  }

  return results.toList()
}

private fun isPotentialBrandToken(
    value: String,
): Boolean {
  if (value.length < 3) {
    return false
  }

  if (value.length > 15) {
    return false
  }

  if (value.all { it.isDigit() }) {
    return false
  }

  return value.any { it.isLetter() }
}

private fun looksLikeDomain(
    value: String,
): Boolean {
  return value.contains(".") && value.split(".").size >= 2
}

private fun looksLikeUrl(
    value: String,
): Boolean {
  return value.startsWith("http", ignoreCase = true) || value.startsWith("www.", ignoreCase = true)
}

private fun looksLikeRandomCode(
    value: String,
): Boolean {
  if (value.length < 4) {
    return false
  }

  val hasLetters = value.any { it.isLetter() }
  val hasDigits = value.any { it.isDigit() }

  /*
   * Examples:
   * A7F92K
   * 847291
   * OTP12345
   *
   * Avoid treating these as companies.
   */
  return hasLetters && hasDigits && value.count { it.isDigit() } >= 3
}

private fun extractUrls(
    text: String,
): List<String> {
  val regex =
      Regex(
          pattern = """(?i)\bhttps?://[^\s<>"']+""",
      )

  return regex
      .findAll(text)
      .map {
        it.value.trimEnd(
            '.',
            ',',
            ')',
            ']',
            '}',
            ';',
            ':',
        )
      }
      .toList()
}

private fun extractEmailDomains(
    text: String,
): List<String> {
  val regex =
      Regex(
          pattern = """(?i)\b[A-Z0-9._%+-]+@([A-Z0-9.-]+\.[A-Z]{2,})\b""",
      )

  return regex
      .findAll(text)
      .mapNotNull { match ->
        normalizeDomain(match.groupValues[1])
      }
      .toList()
}

private fun extractPlainDomains(
    text: String,
): List<String> {
  val regex =
      Regex(
          pattern = """(?i)\b(?:www\.)?[A-Z0-9-]+(?:\.[A-Z0-9-]+)+\b""",
      )

  return regex
      .findAll(text)
      .mapNotNull { match ->
        normalizeDomain(match.value)
      }
      .toList()
}

private fun normalizeDomain(
    value: String,
): String? {
  var candidate = value.trim()

  if (candidate.isBlank()) {
    return null
  }

  candidate =
      if (
          candidate.startsWith(
              "http://",
              ignoreCase = true,
          ) ||
              candidate.startsWith(
                  "https://",
                  ignoreCase = true,
              )
      ) {
        candidate
      } else {
        "https://$candidate"
      }

  val host =
      runCatching {
        Uri.parse(candidate).host
      }
          .getOrNull() ?: return null

  var domain =
      host.lowercase(
          Locale.getDefault(),
      )

  if (domain.startsWith("www.")) {
    domain = domain.removePrefix("www.")
  }

  if (!isValidDomain(domain)) {
    return null
  }

  return domain
}

private fun isValidDomain(
    domain: String,
): Boolean {
  if (domain.length < 4 || domain.length > 253) {
    return false
  }

  if (!domain.contains(".")) {
    return false
  }

  if (domain.startsWith(".") || domain.endsWith(".")) {
    return false
  }

  return domain.split(".").all { part ->
    part.isNotBlank() &&
        part.length <= 63 &&
        part.first().isLetterOrDigit() &&
        part.last().isLetterOrDigit() &&
        part.all { character ->
          character.isLetterOrDigit() || character == '-'
        }
  }
}
