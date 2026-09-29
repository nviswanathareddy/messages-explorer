package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import com.nviswanathareddy.messagesexplorer.utils.scaledSp
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun DateSection(
    selectedDateMillis: Long,
    palette: AppPalette,
    fontScale: Float,
    onPreviousDay: () -> Unit,
    onNextDay: () -> Unit,
    onDateClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(72.dp)
                    .clickable(onClick = onPreviousDay),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronLeft,
                    contentDescription = "Previous Day",
                    modifier = Modifier.width(22.dp),
                    tint = palette.primaryDark
                )
            }

            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(72.dp)
                    .clickable(onClick = onDateClick),
                color = Color(0xFFEFF4FC),
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = SimpleDateFormat(
                            "EEEE",
                            LocalLocale.current.platformLocale
                        )
                            .format(Date(selectedDateMillis))
                            .uppercase(LocalLocale.current.platformLocale),
                        color = Color(0xFF54607B),
                        fontSize = scaledSp(
                            11f,
                            fontScale
                        ),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.5.sp
                    )

                    Text(
                        text = SimpleDateFormat(
                            "dd MMMM yyyy",
                            LocalLocale.current.platformLocale
                        ).format(Date(selectedDateMillis)),
                        color = Color(0xFF0A1C36),
                        fontSize = scaledSp(
                            20f,
                            fontScale
                        ),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp,
                        modifier = Modifier.padding(top = 0.5.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(72.dp)
                    .clickable(onClick = onNextDay),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "Next Day",
                    modifier = Modifier.width(22.dp),
                    tint = palette.primaryDark
                )
            }
        }
    }
}