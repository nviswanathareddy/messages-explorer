package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.utils.AppFontCardBody
import com.nviswanathareddy.messagesexplorer.utils.AppFontCardTitle
import com.nviswanathareddy.messagesexplorer.utils.AppLargeCardRadius
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

@Composable
fun PermissionCard(
    fontScale: Float,
    darkMode: Boolean,
    onAllow: () -> Unit
) {
    val palette = if (darkMode) DarkPalette else LightPalette
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(AppLargeCardRadius),
        colors = CardDefaults.cardColors(containerColor = palette.dateCardBackground)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "SMS permission required",
                fontSize = scaledSp(AppFontCardTitle, fontScale),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Allow Messages Explorer to read your SMS messages so they can be displayed by date.",
                fontSize = scaledSp(AppFontCardBody, fontScale)
            )
            Spacer(modifier = Modifier.height(14.dp))
            TextButton(onClick = onAllow) {
                Text(text = "Allow SMS access")
            }
        }
    }
}

@Composable
fun EmptyMessagesCard(
    searchQuery: String,
    fontScale: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(AppLargeCardRadius)
    ) {
        Column(
            modifier = Modifier.padding(26.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (searchQuery.isBlank()) {
                    "No messages found"
                } else {
                    "No matching messages"
                },
                fontSize = scaledSp(AppFontCardTitle, fontScale),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = if (searchQuery.isBlank()) {
                    "There are no SMS messages for this date."
                } else {
                    "Try a different sender or message."
                },
                fontSize = scaledSp(AppFontCardBody, fontScale)
            )
        }
    }
}

