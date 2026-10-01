package com.nviswanathareddy.messagesexplorer.data
import android.util.Log
import com.nviswanathareddy.messagesexplorer.utils.SenderInfo
import com.nviswanathareddy.messagesexplorer.utils.resolveLogoDomain
class LogoRepository {
  private val domainCache = mutableMapOf<String, String?>()
  fun getLogoDomain(
    senderInfo: SenderInfo,
    body: String
  ): String? {
    val cacheKey = "${senderInfo.key}|${body.hashCode()}"
    if (domainCache.containsKey(cacheKey)) {
      return domainCache[cacheKey]
    }
    val domain = resolveLogoDomain(
      senderInfo = senderInfo,
      body = body
    )
    domainCache[cacheKey] = domain
    Log.d(
      "LogoRepository",
      "Sender=${senderInfo.displayName}, Domain=$domain"
    )
    return domain
  }
  fun clearCache() {
    domainCache.clear()
  }
}