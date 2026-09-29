package com.nviswanathareddy.messagesexplorer.utils

import androidx.compose.ui.graphics.Color
import java.util.Locale

fun categoryColor(
    category: String,
    palette: AppPalette
): Color {
    return when (category) {
        "Payment" -> palette.paymentColor
        "Bank" -> palette.bankColor
        "OTP" -> palette.otpColor
        "Alert" -> palette.alertColor
        "Shopping" -> palette.shoppingColor
        "Service" -> palette.serviceColor
        else -> palette.otherColor
    }
}

fun categoryBackground(
    category: String,
    palette: AppPalette
): Color {
    return when (category) {
        "Payment" -> palette.paymentBackground
        "Bank" -> palette.bankBackground
        "OTP" -> palette.otpBackground
        "Alert" -> palette.alertBackground
        "Shopping" -> palette.shoppingBackground
        "Service" -> palette.serviceBackground
        else -> palette.otherBackground
    }
}

fun detectCategory(
    sender: String,
    body: String
): String {
    val text = "$sender $body".lowercase(Locale.getDefault())

    return when {
        text.contains("otp") ||
                text.contains("one time password") ||
                text.contains("verification code") -> "OTP"

        text.contains("payment") ||
                text.contains("paid") ||
                text.contains("debited") ||
                text.contains("credited") ||
                text.contains("transaction") -> "Payment"

        text.contains("bank") ||
                text.contains("account") ||
                text.contains("balance") ||
                text.contains("upi") ||
                text.contains("ifsc") -> "Bank"

        text.contains("alert") ||
                text.contains("warning") ||
                text.contains("security") ||
                text.contains("login") -> "Alert"

        text.contains("amazon") ||
                text.contains("flipkart") ||
                text.contains("order") ||
                text.contains("delivery") ||
                text.contains("shopping") -> "Shopping"

        text.contains("service") ||
                text.contains("recharge") ||
                text.contains("bill") -> "Service"

        else -> "Other"
    }
}