package com.nviswanathareddy.messagesexplorer.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class AppPalette(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val primaryDark: Color,
    val secondaryText: Color,
    val messageText: Color,
    val controlBackground: Color,
    val dateControlBackground: Color,
    val dateCardBackground: Color,
    val calendarBackground: Color,
    val paymentColor: Color,
    val paymentBackground: Color,
    val bankColor: Color,
    val bankBackground: Color,
    val otpColor: Color,
    val otpBackground: Color,
    val alertColor: Color,
    val alertBackground: Color,
    val shoppingColor: Color,
    val shoppingBackground: Color,
    val serviceColor: Color,
    val serviceBackground: Color,
    val otherColor: Color,
    val otherBackground: Color
)

val LightPalette = AppPalette(
    background = Color(0xFFF9FAFF),
    surface = Color.White,
    primary = Color(0xFF3D36E8),
    primaryDark = Color(0xFF172B55),
    secondaryText = Color(0xFF667085),
    messageText = Color(0xFF374151),
    controlBackground = Color(0xFFF0F2FA),
    dateControlBackground = Color(0xFFE9EDFF),
    dateCardBackground = Color(0xFFF3F5FF),
    calendarBackground = Color(0xFF4B4BF5),
    paymentColor = Color(0xFFE05A78),
    paymentBackground = Color(0xFFFCE9EE),
    bankColor = Color(0xFF319B68),
    bankBackground = Color(0xFFE8F5EE),
    otpColor = Color(0xFF4057D6),
    otpBackground = Color(0xFFEAF0FF),
    alertColor = Color(0xFFD87919),
    alertBackground = Color(0xFFFFF0DE),
    shoppingColor = Color(0xFFD94F70),
    shoppingBackground = Color(0xFFFCE9EE),
    serviceColor = Color(0xFF168E82),
    serviceBackground = Color(0xFFE4F5F2),
    otherColor = Color(0xFF667085),
    otherBackground = Color(0xFFEEF0F4)
)

val DarkPalette = AppPalette(
    background = Color(0xFF0F172A),
    surface = Color(0xFF111827),
    primary = Color(0xFF8B86FF),
    primaryDark = Color(0xFFF1F5F9),
    secondaryText = Color(0xFF94A3B8),
    messageText = Color(0xFFE2E8F0),
    controlBackground = Color(0xFF1E293B),
    dateControlBackground = Color(0xFF263A66),
    dateCardBackground = Color(0xFF172554),
    calendarBackground = Color(0xFF6366F1),
    paymentColor = Color(0xFFF38BAA),
    paymentBackground = Color(0xFF4A2632),
    bankColor = Color(0xFF67D39A),
    bankBackground = Color(0xFF18382A),
    otpColor = Color(0xFF9AA7FF),
    otpBackground = Color(0xFF202A52),
    alertColor = Color(0xFFFFB866),
    alertBackground = Color(0xFF49351F),
    shoppingColor = Color(0xFFF58CA8),
    shoppingBackground = Color(0xFF4A2632),
    serviceColor = Color(0xFF65D2C5),
    serviceBackground = Color(0xFF173B38),
    otherColor = Color(0xFFCBD5E1),
    otherBackground = Color(0xFF263241)
)

const val PreferencesName = "messages_explorer_preferences"
const val PreferenceDarkMode = "dark_mode"
const val PreferenceFontScale = "font_scale"

val AppCardRadius = 12.dp
val AppLargeCardRadius = 12.dp
val AppPillRadius = 20.dp
val AppDateControlSize = 52.dp
val AppHeaderControlSize = 40.dp
val AppScreenHorizontalPadding = 16.dp
val AppHeaderHorizontalPadding = 16.dp
val AppMessagePadding = 14.dp
val AppCategoryRadius = 12.dp
val AppCategoryHorizontalPadding = 12.dp
val AppCategoryVerticalPadding = 4.dp
val AppCategoryGap = 8.dp
val AppHeaderMinHeight = 0.dp
val AppHomeDateCardHeight = 58.dp
val AppHomeDateArrowSize = 34.dp
val AppHomeDateArrowIconSize = 20.dp
val AppHomeMessageIconSize = 34.dp
val AppHomeMessageCardRadius = 14.dp
val AppHomeMessagePadding = 12.dp
const val AppPopupHorizontalInsetFraction = 0.10f
val AppPopupTopSpacing = 12.dp
val AppPopupHeaderOffset = 132.dp
val AppPopupCornerRadius = 24.dp
val AppPopupBlurRadius = 8.dp

const val AppFontTopTitle = 22f
const val AppFontSubtitle = 14f
const val AppFontDate = 16f
const val AppFontCount = 16f
const val AppFontCountLabel = 16f
const val AppFontSort = 12f
const val AppFontSender = 14f
const val AppFontTime = 12f
const val AppFontMessage = 14f
const val AppFontMessageLineHeight = 20f
const val AppFontHint = 12f
const val AppFontCategory = 12f
const val AppFontMenu = 14f
const val AppFontBottomNavigation = 12f
const val AppFontSearch = 14f
const val AppFontCardTitle = 16f
const val AppFontCardBody = 16f


fun scaledSp(baseSize: Float, scale: Float) = (baseSize * scale).sp

val AppFontHeaderTitle = 24f
