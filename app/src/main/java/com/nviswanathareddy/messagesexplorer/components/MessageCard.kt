package com.nviswanathareddy.messagesexplorer.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.data.LogoRepository
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.TransactionInfo
import com.nviswanathareddy.messagesexplorer.utils.TransactionType
import com.nviswanathareddy.messagesexplorer.utils.detectCategory
import com.nviswanathareddy.messagesexplorer.utils.detectTransaction
import com.nviswanathareddy.messagesexplorer.utils.resolveSenderInfo
import com.nviswanathareddy.messagesexplorer.utils.scaledSp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val CreditColor = Color(0xFF16A34A)
private val CreditBackground = Color(0xFFE3F7EA)
private val DebitColor = Color(0xFFDC2626)
private val DebitBackground = Color(0xFFFDE7E7)

@Composable
fun MessageCard(
    message: SmsMessage,
    fontScale: Float,
    darkMode: Boolean,
    expanded: Boolean,
    showDate: Boolean,
    logoRepository: LogoRepository,
    onClick: () -> Unit,
) {
  val context = LocalContext.current
  val palette = if (darkMode) DarkPalette else LightPalette

  val category =
      remember(message.id) {
        detectCategory(
            message.sender,
            message.body,
        )
      }

  val transaction =
      remember(message.id) {
        detectTransaction(
            message.sender,
            message.body,
        )
      }

  val senderInfo =
      remember(message.id) {
        resolveSenderInfo(
            sender = message.sender,
            body = message.body,
        )
      }

  val dateTimeLabel =
      remember(
          message.timestamp,
          showDate,
      ) {
        val pattern =
            if (showDate) {
              "MMM dd yyyy · hh:mm a"
            } else {
              "hh:mm a"
            }

        SimpleDateFormat(
                pattern,
                Locale.ENGLISH,
            )
            .format(Date(message.timestamp))
      }

  Card(
      modifier =
          Modifier.fillMaxWidth()
              .combinedClickable(
                  onClick = onClick,
                  onLongClick = {
                    copyMessageToClipboard(
                        context = context,
                        message = message.body,
                    )
                  },
              ),
      shape = RoundedCornerShape(12.dp),
      colors =
          CardDefaults.cardColors(
              containerColor = palette.surface,
          ),
      elevation =
          CardDefaults.cardElevation(
              defaultElevation = 1.dp,
          ),
  ) {
    Column(
        modifier =
            Modifier.padding(
                horizontal = 16.dp,
                vertical = 10.dp,
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        SenderLogo(
            senderInfo = senderInfo,
            body = message.body,
            logoRepository = logoRepository,
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = message.sender,
            modifier =
                Modifier.weight(1f)
                    .combinedClickable(
                        onClick = onClick,
                        onLongClick = onClick,
                    ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize =
                scaledSp(
                    14f,
                    fontScale,
                ),
            fontWeight = FontWeight.Bold,
            color = palette.primaryDark,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = dateTimeLabel,
            maxLines = 1,
            color = palette.secondaryText,
            fontSize =
                scaledSp(
                    12f,
                    fontScale,
                ),
            fontWeight = FontWeight.Medium,
        )
      }

      MessageBody(
          body = message.body,
          fontScale = fontScale,
          palette = palette,
          expanded = expanded,
          onClick = onClick,
          onLongPress = {
            copyMessageToClipboard(
                context = context,
                message = message.body,
            )
          },
      )

      HorizontalDivider(
          color = palette.secondaryText.copy(alpha = 0.12f),
          thickness = 1.dp,
      )

      Row(
          modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
          verticalAlignment = Alignment.CenterVertically,
      ) {
        TransactionSummary(
            transaction = transaction,
            fontScale = fontScale,
            palette = palette,
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text =
                if (expanded) {
                  "Tap to collapse"
                } else {
                  "Tap to expand"
                },
            color = palette.primary,
            fontSize =
                scaledSp(
                    11f,
                    fontScale,
                ),
            fontWeight = FontWeight.Medium,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.width(2.dp))

        Icon(
            imageVector =
                if (expanded) {
                  Icons.Outlined.ExpandLess
                } else {
                  Icons.Outlined.ExpandMore
                },
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = palette.primary,
        )
      }
    }
  }
}

@Composable
private fun TransactionSummary(
    transaction: TransactionInfo,
    fontScale: Float,
    palette: AppPalette,
) {
  if (transaction.type == TransactionType.NONE) {
    return
  }

  val typeColor =
      when (transaction.type) {
        TransactionType.CREDIT -> CreditColor
        TransactionType.DEBIT -> DebitColor
        TransactionType.NONE -> palette.secondaryText
      }

  val typeBackground =
      when (transaction.type) {
        TransactionType.CREDIT -> CreditBackground
        TransactionType.DEBIT -> DebitBackground
        TransactionType.NONE -> Color.Transparent
      }

  Row(
      verticalAlignment = Alignment.CenterVertically,
  ) {
    Surface(
        modifier = Modifier.size(24.dp),
        shape = CircleShape,
        color = typeBackground,
    ) {
      Box(contentAlignment = Alignment.Center) {
        Text(
            text = transaction.shortType,
            fontSize =
                scaledSp(
                    12f,
                    fontScale,
                ),
            fontWeight = FontWeight.Bold,
            color = typeColor,
        )
      }
    }

    if (transaction.displayText.isNotBlank()) {
      val detailText = transaction.displayText.removePrefix(transaction.shortType).trim()

      if (detailText.isNotBlank()) {
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = detailText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize =
                scaledSp(
                    12f,
                    fontScale,
                ),
            fontWeight = FontWeight.Medium,
            color = palette.secondaryText,
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
    expanded: Boolean,
    onClick: () -> Unit,
    onLongPress: () -> Unit,
) {
  val context = LocalContext.current

  var textLayoutResult by remember {
    mutableStateOf<TextLayoutResult?>(null)
  }

  val annotatedText =
      remember(
          body,
          palette.primary,
      ) {
        buildMessageAnnotatedString(
            body = body,
            palette = palette,
        )
      }

  Text(
      text = annotatedText,
      modifier =
          Modifier.fillMaxWidth().pointerInput(
              annotatedText,
              expanded,
          ) {
            detectTapGestures(
                onTap = { position ->
                  textLayoutResult?.let { layoutResult ->
                    val offset = layoutResult.getOffsetForPosition(position)

                    val annotation =
                        annotatedText
                            .getStringAnnotations(
                                tag = "URL",
                                start = offset,
                                end = offset,
                            )
                            .firstOrNull()

                    if (annotation != null) {
                      openUrlInChrome(
                          context = context,
                          url = annotation.item,
                      )
                    } else {
                      onClick()
                    }
                  } ?: onClick()
                },
                onLongPress = {
                  onLongPress()
                },
            )
          },
      maxLines =
          if (expanded) {
            Int.MAX_VALUE
          } else {
            2
          },
      overflow =
          if (expanded) {
            TextOverflow.Visible
          } else {
            TextOverflow.Ellipsis
          },
      style =
          TextStyle(
              color = palette.messageText,
              fontSize =
                  scaledSp(
                      12f,
                      fontScale,
                  ),
              lineHeight =
                  scaledSp(
                      18f,
                      fontScale,
                  ),
          ),
      onTextLayout = {
        textLayoutResult = it
      },
  )
}

private fun buildMessageAnnotatedString(
    body: String,
    palette: AppPalette,
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
                start,
            )
        )
      }

      val detectedUrl =
          body.substring(
              start,
              end,
          )

      val url =
          if (detectedUrl.startsWith("http://") || detectedUrl.startsWith("https://")) {
            detectedUrl
          } else {
            "https://$detectedUrl"
          }

      pushStringAnnotation(
          tag = "URL",
          annotation = url,
      )

      pushStyle(
          SpanStyle(
              color = palette.primary,
              textDecoration = TextDecoration.Underline,
          )
      )

      append(detectedUrl)

      pop()
      pop()

      currentIndex = end
    }

    if (currentIndex < body.length) {
      append(body.substring(currentIndex))
    }
  }
}

private fun openUrlInChrome(
    context: Context,
    url: String,
) {
  val chromeIntent =
      Intent(
              Intent.ACTION_VIEW,
              Uri.parse(url),
          )
          .apply {
            setPackage("com.android.chrome")
          }

  try {
    context.startActivity(chromeIntent)
  } catch (_: Exception) {
    context.startActivity(
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse(url),
        )
    )
  }
}

private fun copyMessageToClipboard(
    context: Context,
    message: String,
) {
  val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

  clipboard.setPrimaryClip(
      ClipData.newPlainText(
          "Message",
          message,
      )
  )

  android.widget.Toast.makeText(
          context,
          "Message copied",
          android.widget.Toast.LENGTH_SHORT,
      )
      .show()
}
