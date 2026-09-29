package com.nviswanathareddy.messagesexplorer.model

enum class MessageSort { NEWEST_FIRST, OLDEST_FIRST, SENDER_A_TO_Z }

data class SmsMessage(
    val id: Long,
    val sender: String,
    val address: String,
    val body: String,
    val timestamp: Long
)
