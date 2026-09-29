package com.nviswanathareddy.messagesexplorer.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.data.formatTime
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.categoryBackground
import com.nviswanathareddy.messagesexplorer.utils.categoryColor
import com.nviswanathareddy.messagesexplorer.utils.detectCategory
import com.nviswanathareddy.messagesexplorer.utils.scaledSp

@Composable
fun MessageCard(
    message: SmsMessage,
    fontScale: Float,
    darkMode: Boolean,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    val palette = if (darkMode) DarkPalette else LightPalette
    val category = detectCategory(
        message.sender,
        message.body
    )

    val categoryIcon = when (category) {
        "OTP" -> Icons.Outlined.Security
        "Bank" -> Icons.Outlined.AccountBalance
        "Payment" -> Icons.Outlined.AccountBalance
        "Shopping" -> Icons.Outlined.LocalShipping
        "Service" -> Icons.Outlined.AccountBalance
        else -> Icons.Outlined.PersonOutline
    }

    val iconColor = categoryColor(
        category,
        palette
    )

    val iconBackground = categoryBackground(
        category,
        palette
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = {
                    copyMessageToClipboard(
                        context = context,
                        message = message.body
                    )
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = palette.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = iconBackground
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = categoryIcon,
                            contentDescription = category,
                            modifier = Modifier.size(20.dp),
                            tint = iconColor
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message.sender,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = scaledSp(
                            14f,
                            fontScale
                        ),
                        fontWeight = FontWeight.Bold,
                        color = palette.primaryDark
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Surface(
                        shape = CircleShape,
                        color = iconBackground
                    ) {
                        Text(
                            text = category.uppercase(
                                LocalLocale.current.platformLocale
                            ),
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 3.dp
                            ),
                            fontSize = scaledSp(
                                10f,
                                fontScale
                            ),
                            fontWeight = FontWeight.SemiBold,
                            color = iconColor
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                Text(
                    text = formatTime(
                        message.timestamp
                    ),
                    maxLines = 1,
                    color = palette.primary,
                    fontSize = scaledSp(
                        12f,
                        fontScale
                    ),
                    fontWeight = FontWeight.Medium
                )
            }

            MessageBody(
                body = message.body,
                fontScale = fontScale,
                palette = palette,
                expanded = expanded
            )

            HorizontalDivider(
                color = palette.secondaryText.copy(
                    alpha = 0.12f
                ),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = onClick
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) {
                        "Tap to collapse"
                    } else {
                        "Tap to expand"
                    },
                    color = palette.primary,
                    fontSize = scaledSp(
                        11f,
                        fontScale
                    ),
                    fontWeight = FontWeight.Medium
                )

                Spacer(
                    modifier = Modifier.width(2.dp)
                )

                Icon(
                    imageVector = if (expanded) {
                        Icons.Outlined.ExpandLess
                    } else {
                        Icons.Outlined.ExpandMore
                    },
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = palette.primary
                )
            }
        }
    }
}

@Composable
private fun MessageBody(
    body: String,
    fontScale: Float,
    palette: AppPalette,
    expanded: Boolean
) {
    val context = LocalContext.current

    val annotatedText = buildMessageAnnotatedString(
        body = body,
        palette = palette
    )

    ClickableText(
        text = annotatedText,
        modifier = Modifier.fillMaxWidth(),
        maxLines = if (expanded) {
            Int.MAX_VALUE
        } else {
            2
        },
        overflow = if (expanded) {
            TextOverflow.Visible
        } else {
            TextOverflow.Ellipsis
        },
        style = TextStyle(
            color = palette.messageText,
            fontSize = scaledSp(
                12f,
                fontScale
            ),
            lineHeight = scaledSp(
                18f,
                fontScale
            )
        ),
        onClick = { offset ->
            annotatedText
                .getStringAnnotations(
                    tag = "URL",
                    start = offset,
                    end = offset
                )
                .firstOrNull()
                ?.let { annotation ->
                    openUrlInChrome(
                        context = context,
                        url = annotation.item
                    )
                }
        }
    )
}

private fun buildMessageAnnotatedString(
    body: String,
    palette: AppPalette
): AnnotatedString {
    return buildAnnotatedString {
        val matcher = Patterns.WEB_URL.matcher(body)
        var currentIndex = 0

        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()

            if (start > currentIndex) {
                append(
                    body.substring(
                        currentIndex,
                        start
                    )
                )
            }

            val detectedUrl = body.substring(
                start,
                end
            )

            val url = if (
                detectedUrl.startsWith("http://") ||
                detectedUrl.startsWith("https://")
            ) {
                detectedUrl
            } else {
                "https://$detectedUrl"
            }

            pushStringAnnotation(
                tag = "URL",
                annotation = url
            )

            pushStyle(
                SpanStyle(
                    color = palette.primary,
                    textDecoration = TextDecoration.Underline
                )
            )

            append(detectedUrl)

            pop()
            pop()

            currentIndex = end
        }

        if (currentIndex < body.length) {
            append(
                body.substring(currentIndex)
            )
        }
    }
}

private fun openUrlInChrome(
    context: Context,
    url: String
) {
    val chromeIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(url)
    ).apply {
        setPackage("com.android.chrome")
    }

    try {
        context.startActivity(
            chromeIntent
        )
    } catch (_: Exception) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
        )
    }
}

private fun copyMessageToClipboard(
    context: Context,
    message: String
) {
    val clipboard = context.getSystemService(
        Context.CLIPBOARD_SERVICE
    ) as ClipboardManager

    clipboard.setPrimaryClip(
        ClipData.newPlainText(
            "Message",
            message
        )
    )

    Toast.makeText(
        context,
        "Message copied",
        Toast.LENGTH_SHORT
    ).show()
}