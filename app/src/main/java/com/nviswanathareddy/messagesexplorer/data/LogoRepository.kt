package com.nviswanathareddy.messagesexplorer.data

import android.util.Log
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.BrandEvidence
import com.nviswanathareddy.messagesexplorer.utils.DomainEvidence
import com.nviswanathareddy.messagesexplorer.utils.SenderInfo
import com.nviswanathareddy.messagesexplorer.utils.extractBrandEvidence
import com.nviswanathareddy.messagesexplorer.utils.extractDomainEvidence
import com.nviswanathareddy.messagesexplorer.utils.normalizeSender

data class SenderLogoMapping(
    val domain: String?,
    val brandName: String?,
)

class LogoRepository {
    private val senderLogoCache =
        mutableMapOf<String, SenderLogoMapping>()

    fun buildSenderDomainMap(
        messages: List<SmsMessage>,
    ) {
        senderLogoCache.clear()

        val messagesBySender =
            messages.groupBy { message ->
                normalizeSender(message.sender)
            }

        messagesBySender.forEach { (senderKey, senderMessages) ->
            val domainEvidence =
                senderMessages
                    .flatMap { message ->
                        extractDomainEvidence(message.body)
                    }
                    .groupBy { evidence ->
                        evidence.domain
                    }
                    .map { (domain, evidenceList) ->
                        DomainEvidence(
                            domain = domain,
                            score = evidenceList.sumOf { it.score },
                        )
                    }
                    .sortedByDescending { evidence ->
                        evidence.score
                    }

            val brandEvidence =
                senderMessages
                    .flatMap { message ->
                        extractBrandEvidence(
                            sender = message.sender,
                            body = message.body,
                        )
                    }
                    .groupBy { evidence ->
                        evidence.name.lowercase()
                    }
                    .map { (_, evidenceList) ->
                        BrandEvidence(
                            name =
                                evidenceList
                                    .maxByOrNull { it.name.length }
                                    ?.name
                                    ?: return@map null,
                            score = evidenceList.sumOf { it.score },
                        )
                    }
                    .filterNotNull()
                    .sortedByDescending { evidence ->
                        evidence.score
                    }

            val selectedDomain =
                domainEvidence
                    .firstOrNull()
                    ?.domain

            val selectedBrand =
                brandEvidence
                    .firstOrNull()
                    ?.name

            senderLogoCache[senderKey] =
                SenderLogoMapping(
                    domain = selectedDomain,
                    brandName = selectedBrand,
                )

            Log.d(
                "LogoRepository",
                "Sender=$senderKey Domain=$selectedDomain Brand=$selectedBrand Messages=${senderMessages.size}",
            )
        }
    }

    fun getLogoResolution(
        senderInfo: SenderInfo,
        body: String,
    ): com.nviswanathareddy.messagesexplorer.utils.LogoResolution {
        val senderKey = senderInfo.key

        val cached =
            senderLogoCache[senderKey]

        if (cached != null) {
            return com.nviswanathareddy.messagesexplorer.utils.LogoResolution(
                domain = cached.domain,
                brandName = cached.brandName,
            )
        }

        val messageDomains =
            extractDomainEvidence(body)

        val messageDomain =
            messageDomains
                .maxByOrNull { evidence ->
                    evidence.score
                }
                ?.domain

        val brandEvidence =
            extractBrandEvidence(
                sender = senderInfo.displayName,
                body = body,
            )

        val messageBrand =
            brandEvidence
                .maxByOrNull { evidence ->
                    evidence.score
                }
                ?.name

        val result =
            SenderLogoMapping(
                domain = messageDomain,
                brandName = messageBrand,
            )

        senderLogoCache[senderKey] = result

        Log.d(
            "LogoRepository",
            "Fallback sender=$senderKey Domain=$messageDomain Brand=$messageBrand",
        )

        return com.nviswanathareddy.messagesexplorer.utils.LogoResolution(
            domain = result.domain,
            brandName = result.brandName,
        )
    }

    fun getLogoDomain(
        senderInfo: SenderInfo,
        body: String,
    ): String? {
        return getLogoResolution(
            senderInfo = senderInfo,
            body = body,
        ).domain
    }

    fun getBrandName(
        senderInfo: SenderInfo,
        body: String,
    ): String? {
        return getLogoResolution(
            senderInfo = senderInfo,
            body = body,
        ).brandName
    }

    fun hasSenderDomain(
        senderInfo: SenderInfo,
    ): Boolean {
        return senderLogoCache[senderInfo.key]?.domain != null
    }

    fun clearCache() {
        senderLogoCache.clear()
    }
}