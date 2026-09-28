package com.nviswanathareddy.messagesexplorer

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.PhoneNumberUtils
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import com.nviswanathareddy.messagesexplorer.ui.theme.MessagesExplorerTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private data class AppPalette(
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

private val LightPalette = AppPalette(
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

private val DarkPalette = AppPalette(
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

private const val PreferencesName = "messages_explorer_preferences"
private const val PreferenceDarkMode = "dark_mode"
private const val PreferenceFontScale = "font_scale"

private val AppCardRadius = 12.dp
private val AppLargeCardRadius = 12.dp
private val AppPillRadius = 20.dp
private val AppDateControlSize = 52.dp
private val AppDateCalendarSize = 48.dp
private val AppHeaderControlSize = 40.dp
private val AppScreenHorizontalPadding = 16.dp
private val AppHeaderHorizontalPadding = 16.dp
private val AppMessagePadding = 14.dp
private val AppCategoryRadius = 12.dp
private val AppCategoryHorizontalPadding = 12.dp
private val AppCategoryVerticalPadding = 4.dp
private val AppCategoryGap = 8.dp
private val AppHeaderMinHeight = 88.dp
private val AppPopupWidth = 220.dp
private val AppPopupTopSpacing = 72.dp

private const val AppFontTopTitle = 22f
private const val AppFontSubtitle = 14f
private const val AppFontDate = 16f
private const val AppFontCount = 16f
private const val AppFontCountLabel = 16f
private const val AppFontSort = 12f
private const val AppFontSender = 14f
private const val AppFontTime = 12f
private const val AppFontMessage = 14f
private const val AppFontMessageLineHeight = 20f
private const val AppFontHint = 12f
private const val AppFontCategory = 12f
private const val AppFontMenu = 14f
private const val AppFontBottomNavigation = 12f
private const val AppFontSearch = 14f
private const val AppFontCardTitle = 16f
private const val AppFontCardBody = 16f

private fun scaledSp(baseSize: Float, scale: Float) = (baseSize * scale).sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
            true
        setContent {
            MessagesExplorerSettingsHost()
        }
    }
}

@Composable
private fun MessagesExplorerSettingsHost() {
    val context = LocalContext.current
    val preferences = remember {
        context.getSharedPreferences(
            PreferencesName,
            Context.MODE_PRIVATE
        )
    }
    var darkMode by remember {
        mutableStateOf(
            preferences.getBoolean(
                PreferenceDarkMode,
                false
            )
        )
    }
    var fontScale by remember {
        mutableFloatStateOf(
            preferences.getFloat(
                PreferenceFontScale,
                1f
            )
        )
    }
    val activity = context as? Activity

    SideEffect {
        activity?.window?.let { window ->
            val controller = WindowCompat.getInsetsController(
                window,
                window.decorView
            )
            controller.isAppearanceLightStatusBars = !darkMode
            controller.isAppearanceLightNavigationBars = !darkMode
            controller.systemBarsBehavior =
                androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            // Keep the app edge-to-edge and reveal system navigation controls only on swipe.
            controller.hide(
                androidx.core.view.WindowInsetsCompat.Type.navigationBars()
            )
        }
    }

    MessagesExplorerTheme(
        darkTheme = darkMode
    ) {
        MessagesExplorerScreen(
            darkMode = darkMode,
            fontScale = fontScale,
            onDarkModeChange = { enabled ->
                darkMode = enabled
                preferences.edit {
                    putBoolean(
                        PreferenceDarkMode,
                        enabled
                    )
                }
            },
            onFontScaleChange = { scale ->
                fontScale = scale
                preferences.edit {
                    putFloat(
                        PreferenceFontScale,
                        scale
                    )
                }
            }
        )
    }
}

enum class MessageSort {
    NEWEST_FIRST,
    OLDEST_FIRST,
    SENDER_A_TO_Z
}

data class SmsMessage(
    val id: Long,
    val sender: String,
    val address: String,
    val body: String,
    val timestamp: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagesExplorerScreen(
    darkMode: Boolean,
    fontScale: Float,
    onDarkModeChange: (Boolean) -> Unit,
    onFontScaleChange: (Float) -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val palette = if (darkMode) DarkPalette else LightPalette
    var hasSmsPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var hasContactsPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasSmsPermission = permissions[Manifest.permission.READ_SMS] == true ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_SMS
                ) == PackageManager.PERMISSION_GRANTED
        hasContactsPermission = permissions[Manifest.permission.READ_CONTACTS] == true ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_CONTACTS
                ) == PackageManager.PERMISSION_GRANTED
    }
    val contactsPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasContactsPermission = granted
    }

    LaunchedEffect(hasSmsPermission, hasContactsPermission) {
        if (hasSmsPermission && !hasContactsPermission) {
            contactsPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }
    var selectedDateMillis by remember {
        mutableLongStateOf(startOfDayMillis(Calendar.getInstance()))
    }
    var messages by remember { mutableStateOf<List<SmsMessage>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var searchOpen by remember { mutableStateOf(false) }
    var sortOption by remember { mutableStateOf(MessageSort.NEWEST_FIRST) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var moreMenuExpanded by remember { mutableStateOf(false) }
    var datePickerOpen by remember { mutableStateOf(false) }
    var expandedMessageId by remember { mutableStateOf<Long?>(null) }
    val listState = rememberLazyListState()

    LaunchedEffect(selectedDateMillis, hasSmsPermission, hasContactsPermission) {
        messages = if (hasSmsPermission) {
            readSmsForDate(
                context = context,
                selectedDateMillis = selectedDateMillis,
                hasContactsPermission = hasContactsPermission
            )
        } else {
            emptyList()
        }
    }

    LaunchedEffect(sortOption, selectedDateMillis, searchQuery) {
        listState.scrollToItem(0)
        expandedMessageId = null
    }

    val filteredMessages = remember(messages, searchQuery, sortOption) {
        val searchedMessages = if (searchQuery.isBlank()) {
            messages
        } else {
            messages.filter { message ->
                message.sender.contains(searchQuery, ignoreCase = true) ||
                        message.address.contains(searchQuery, ignoreCase = true) ||
                        message.body.contains(searchQuery, ignoreCase = true)
            }
        }
        when (sortOption) {
            MessageSort.NEWEST_FIRST -> searchedMessages.sortedByDescending { it.timestamp }
            MessageSort.OLDEST_FIRST -> searchedMessages.sortedBy { it.timestamp }
            MessageSort.SENDER_A_TO_Z -> searchedMessages.sortedWith(
                compareBy(
                    { it.sender.lowercase(Locale.getDefault()) },
                    { -it.timestamp }
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = palette.background,
            topBar = {
                Column(modifier = Modifier.statusBarsPadding()) {
                    if (searchOpen) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(
                                    fontSize = scaledSp(AppFontSearch, fontScale)
                                ),
                                singleLine = true,
                                placeholder = {
                                    Text(
                                        text = "Search sender or message",
                                        fontSize = scaledSp(AppFontSearch, fontScale)
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Outlined.Search,
                                        contentDescription = null
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            TextButton(
                                onClick = {
                                    searchQuery = ""
                                    searchOpen = false
                                }
                            ) {
                                Text(
                                    text = "Cancel",
                                    fontSize = scaledSp(AppFontSearch, fontScale)
                                )
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = AppHeaderMinHeight)
                                .padding(horizontal = AppHeaderHorizontalPadding),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Messages",
                                    color = palette.primary,
                                    fontSize = scaledSp(AppFontTopTitle, fontScale),
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Explorer",
                                    color = palette.primaryDark,
                                    fontSize = scaledSp(AppFontTopTitle, fontScale),
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Browse your messages by date",
                                    color = palette.secondaryText,
                                    fontSize = scaledSp(AppFontSubtitle, fontScale),
                                    maxLines = 1
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(AppHeaderControlSize)
                                        .clickable { searchOpen = true },
                                    shape = CircleShape,
                                    color = palette.controlBackground
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.Search,
                                            contentDescription = "Search",
                                            tint = palette.primaryDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    modifier = Modifier
                                        .size(AppHeaderControlSize)
                                        .clickable {
                                            selectedDateMillis =
                                                startOfDayMillis(Calendar.getInstance())
                                        },
                                    shape = CircleShape,
                                    color = palette.controlBackground
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Outlined.CalendarMonth,
                                            contentDescription = "Go to today",
                                            tint = palette.primaryDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    modifier = Modifier.size(AppHeaderControlSize),
                                    shape = CircleShape,
                                    color = palette.controlBackground
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        IconButton(onClick = { moreMenuExpanded = true }) {
                                            Icon(
                                                imageVector = Icons.Outlined.MoreVert,
                                                contentDescription = "More options",
                                                tint = palette.primaryDark,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                Surface(
                    modifier = Modifier.navigationBarsPadding(),
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(68.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Surface(
                                shape = CircleShape,
                                color = palette.dateControlBackground
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Home,
                                    contentDescription = "Messages",
                                    modifier = Modifier
                                        .padding(horizontal = 18.dp, vertical = 5.dp)
                                        .size(22.dp),
                                    tint = palette.primaryDark
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Messages",
                                fontSize = scaledSp(AppFontBottomNavigation, fontScale),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = AppScreenHorizontalPadding)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(AppLargeCardRadius),
                    colors = CardDefaults.cardColors(
                        containerColor = palette.dateCardBackground
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(AppDateControlSize),
                            shape = CircleShape,
                            color = palette.dateControlBackground
                        ) {
                            IconButton(
                                onClick = {
                                    selectedDateMillis = addDays(selectedDateMillis, -1)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChevronLeft,
                                    contentDescription = "Previous day",
                                    tint = palette.primaryDark,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { datePickerOpen = true },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(AppDateCalendarSize),
                                shape = RoundedCornerShape(9.dp),
                                color = palette.calendarBackground
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarMonth,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = SimpleDateFormat(
                                        "EEEE",
                                        LocalLocale.current.platformLocale
                                    ).format(Date(selectedDateMillis)),
                                    fontSize = scaledSp(AppFontSubtitle, fontScale),
                                    color = palette.secondaryText,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = SimpleDateFormat(
                                        "dd MMMM yyyy",
                                        LocalLocale.current.platformLocale
                                    ).format(Date(selectedDateMillis)),
                                    fontSize = scaledSp(AppFontDate, fontScale),
                                    color = palette.primaryDark,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier.size(AppDateControlSize),
                            shape = CircleShape,
                            color = palette.dateControlBackground
                        ) {
                            IconButton(
                                onClick = {
                                    selectedDateMillis = addDays(selectedDateMillis, 1)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ChevronRight,
                                    contentDescription = "Next day",
                                    tint = palette.primaryDark,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = filteredMessages.size.toString(),
                            color = palette.primary,
                            fontSize = scaledSp(AppFontCount, fontScale),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "received SMS",
                            color = palette.secondaryText,
                            fontSize = scaledSp(AppFontCountLabel, fontScale)
                        )
                    }
                    Box {
                        Surface(
                            modifier = Modifier.clickable { sortMenuExpanded = true },
                            shape = RoundedCornerShape(AppPillRadius),
                            color = palette.controlBackground
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = when (sortOption) {
                                        MessageSort.NEWEST_FIRST -> "Sort · Newest first"
                                        MessageSort.OLDEST_FIRST -> "Sort · Oldest first"
                                        MessageSort.SENDER_A_TO_Z -> "Sort · Sender A → Z"
                                    },
                                    color = palette.primary,
                                    fontSize = scaledSp(AppFontSort, fontScale),
                                    fontWeight = FontWeight.Medium
                                )
                                Icon(
                                    imageVector = Icons.Outlined.ArrowDropDown,
                                    contentDescription = "Sort",
                                    tint = palette.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        DropdownMenu(
                            expanded = sortMenuExpanded,
                            onDismissRequest = { sortMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Newest first",
                                        fontSize = scaledSp(AppFontMenu, fontScale)
                                    )
                                },
                                onClick = {
                                    sortOption = MessageSort.NEWEST_FIRST
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Oldest first",
                                        fontSize = scaledSp(AppFontMenu, fontScale)
                                    )
                                },
                                onClick = {
                                    sortOption = MessageSort.OLDEST_FIRST
                                    sortMenuExpanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = "Sender A → Z",
                                        fontSize = scaledSp(AppFontMenu, fontScale)
                                    )
                                },
                                onClick = {
                                    sortOption = MessageSort.SENDER_A_TO_Z
                                    sortMenuExpanded = false
                                }
                            )
                        }
                    }
                }
                if (!hasSmsPermission) {
                    PermissionCard(
                        fontScale = fontScale,
                        darkMode = darkMode,
                        onAllow = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.READ_CONTACTS
                                )
                            )
                        }
                    )
                } else if (filteredMessages.isEmpty()) {
                    EmptyMessagesCard(
                        searchQuery = searchQuery,
                        fontScale = fontScale
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(
                            items = filteredMessages,
                            key = { it.id }
                        ) { message ->
                            MessageCard(
                                message = message,
                                fontScale = fontScale,
                                darkMode = darkMode,
                                expanded = expandedMessageId == message.id,
                                onClick = {
                                    expandedMessageId = if (expandedMessageId == message.id) {
                                        null
                                    } else {
                                        message.id
                                    }
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }

        if (moreMenuExpanded) {
            Popup(
                alignment = Alignment.TopCenter,
                offset = IntOffset(
                    x = 0,
                    y = WindowInsets.statusBars.getTop(density) +
                            with(density) { AppPopupTopSpacing.roundToPx() }
                ),
                onDismissRequest = { moreMenuExpanded = false },
                properties = PopupProperties(focusable = true)
            ) {
                MoreMenu(
                    fontScale = fontScale,
                    darkMode = darkMode,
                    onDecreaseFont = {
                        onFontScaleChange((fontScale - 0.1f).coerceAtLeast(0.8f))
                    },
                    onResetFont = { onFontScaleChange(1f) },
                    onIncreaseFont = {
                        onFontScaleChange((fontScale + 0.1f).coerceAtMost(1.4f))
                    },
                    onLightMode = { onDarkModeChange(false) },
                    onDarkMode = { onDarkModeChange(true) },
                    onRefresh = {
                        if (hasSmsPermission) {
                            messages = readSmsForDate(
                                context = context,
                                selectedDateMillis = selectedDateMillis,
                                hasContactsPermission = hasContactsPermission
                            )
                        }
                        moreMenuExpanded = false
                    }
                )
            }
        }
    }

    if (datePickerOpen) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis
        )
        DatePickerDialog(
            onDismissRequest = { datePickerOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            selectedDateMillis = startOfDayMillis(
                                Calendar.getInstance().apply {
                                    timeInMillis = millis
                                }
                            )
                        }
                        datePickerOpen = false
                    }
                ) {
                    Text(
                        text = "OK",
                        fontSize = scaledSp(AppFontSearch, fontScale)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { datePickerOpen = false }) {
                    Text(
                        text = "Cancel",
                        fontSize = scaledSp(AppFontSearch, fontScale)
                    )
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun MoreMenu(
    fontScale: Float,
    darkMode: Boolean,
    onDecreaseFont: () -> Unit,
    onResetFont: () -> Unit,
    onIncreaseFont: () -> Unit,
    onLightMode: () -> Unit,
    onDarkMode: () -> Unit,
    onRefresh: () -> Unit
) {
    val palette = if (darkMode) DarkPalette else LightPalette
    Surface(
        modifier = Modifier.width(AppPopupWidth),
        shape = RoundedCornerShape(AppLargeCardRadius),
        color = palette.dateCardBackground,
        shadowElevation = 6.dp
    ) {
        Column {
            DropdownMenuItem(
                text = {
                    Text(
                        text = "Refresh messages",
                        fontSize = scaledSp(AppFontMenu, fontScale)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null
                    )
                },
                onClick = onRefresh
            )
            HorizontalDivider()
            Text(
                text = "TEXT SIZE",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = scaledSp(AppFontHint, fontScale),
                fontWeight = FontWeight.Bold,
                color = palette.secondaryText
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = onDecreaseFont
                ) {
                    Text(
                        text = "A-",
                        fontSize = scaledSp(AppFontMenu, fontScale)
                    )
                }
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = onResetFont
                ) {
                    Text(
                        text = "Reset",
                        fontSize = scaledSp(AppFontMenu, fontScale)
                    )
                }
                TextButton(
                    modifier = Modifier.weight(1f),
                    onClick = onIncreaseFont
                ) {
                    Text(
                        text = "A+",
                        fontSize = scaledSp(AppFontMenu, fontScale)
                    )
                }
            }
            HorizontalDivider()
            Text(
                text = "APPEARANCE",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = scaledSp(AppFontHint, fontScale),
                fontWeight = FontWeight.Bold,
                color = palette.secondaryText
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onLightMode),
                    shape = RoundedCornerShape(AppPillRadius),
                    color = if (!darkMode) palette.controlBackground else Color.Transparent
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "☼ Light",
                            fontSize = scaledSp(AppFontMenu, fontScale),
                            color = palette.primaryDark
                        )
                    }
                }
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onDarkMode),
                    shape = RoundedCornerShape(AppPillRadius),
                    color = if (darkMode) {
                        palette.controlBackground
                    } else {
                        Color.Transparent
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 9.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "◐ Dark",
                            fontSize = scaledSp(AppFontMenu, fontScale),
                            color = palette.primaryDark
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PermissionCard(
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
private fun EmptyMessagesCard(
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

@Composable
private fun MessageCard(
    message: SmsMessage,
    fontScale: Float,
    darkMode: Boolean,
    expanded: Boolean,
    onClick: () -> Unit
) {
    val palette = if (darkMode) DarkPalette else LightPalette
    val category = detectCategory(message.sender, message.body)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(AppCardRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = palette.surface)
    ) {
        Column(modifier = Modifier.padding(AppMessagePadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = message.sender,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = scaledSp(AppFontSender, fontScale),
                    fontWeight = FontWeight.Bold,
                    color = palette.primaryDark
                )
                CategoryText(
                    category = category,
                    fontScale = fontScale,
                    darkMode = darkMode
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = formatTime(message.timestamp),
                    fontSize = scaledSp(AppFontTime, fontScale),
                    color = palette.secondaryText
                )
            }
            Spacer(modifier = Modifier.height(7.dp))
            Text(
                text = message.body,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = if (expanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                fontSize = scaledSp(AppFontMessage, fontScale),
                lineHeight = scaledSp(AppFontMessageLineHeight, fontScale),
                color = palette.messageText
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expanded) "Tap to collapse" else "Tap to expand",
                    fontSize = scaledSp(AppFontHint, fontScale),
                    color = palette.secondaryText
                )
                Spacer(modifier = Modifier.width(2.dp))
                Icon(
                    imageVector = if (expanded) {
                        Icons.Outlined.ExpandLess
                    } else {
                        Icons.Outlined.ExpandMore
                    },
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = palette.secondaryText
                )
            }
        }
    }
}

@Composable
private fun CategoryText(
    category: String,
    fontScale: Float,
    darkMode: Boolean
) {
    val palette = if (darkMode) DarkPalette else LightPalette
    Surface(
        modifier = Modifier.padding(
            start = AppCategoryGap
        ),
        shape = RoundedCornerShape(
            AppCategoryRadius
        ),
        color = categoryBackground(
            category,
            palette
        )
    ) {
        Text(
            text = category.uppercase(LocalLocale.current.platformLocale),
            modifier = Modifier.padding(
                horizontal = AppCategoryHorizontalPadding,
                vertical = AppCategoryVerticalPadding
            ),
            fontSize = scaledSp(
                AppFontCategory,
                fontScale
            ),
            color = categoryColor(
                category,
                palette
            ),
            fontWeight = FontWeight.Bold
        )
    }
}

private fun categoryColor(
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

private fun categoryBackground(
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

private fun detectCategory(sender: String, body: String): String {
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

private fun readSmsForDate(
    context: Context,
    selectedDateMillis: Long,
    hasContactsPermission: Boolean
): List<SmsMessage> {
    val endMillis = addDays(selectedDateMillis, 1)
    val contactNames = if (hasContactsPermission) {
        loadContactNames(context)
    } else {
        emptyMap()
    }
    val result = mutableListOf<SmsMessage>()
    val projection = arrayOf(
        Telephony.Sms._ID,
        Telephony.Sms.ADDRESS,
        Telephony.Sms.BODY,
        Telephony.Sms.DATE
    )
    val selection = "${Telephony.Sms.DATE} >= ? AND ${Telephony.Sms.DATE} < ?"
    val selectionArgs = arrayOf(
        selectedDateMillis.toString(),
        endMillis.toString()
    )

    try {
        context.contentResolver.query(
            Telephony.Sms.Inbox.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${Telephony.Sms.DATE} ASC"
        )?.use { cursor ->
            val idIndex = cursor.getColumnIndexOrThrow(Telephony.Sms._ID)
            val addressIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS)
            val bodyIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.BODY)
            val dateIndex = cursor.getColumnIndexOrThrow(Telephony.Sms.DATE)

            while (cursor.moveToNext()) {
                val address = cursor.getString(addressIndex) ?: "Unknown"
                val normalizedAddress = PhoneNumberUtils.normalizeNumber(address)
                val sender = contactNames[normalizedAddress]
                    ?: normalizedAddress.takeIf { it.length >= 10 }?.let {
                        contactNames[it.takeLast(10)]
                    }
                    ?: address

                result.add(
                    SmsMessage(
                        id = cursor.getLong(idIndex),
                        sender = sender,
                        address = address,
                        body = cursor.getString(bodyIndex) ?: "",
                        timestamp = cursor.getLong(dateIndex)
                    )
                )
            }
        }
    } catch (_: SecurityException) {
        return emptyList()
    }

    return result
}

private fun loadContactNames(context: Context): Map<String, String> {
    val exactMatches = mutableMapOf<String, String>()
    val lastTenCandidates = mutableMapOf<String, MutableSet<String>>()

    return try {
        context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            null
        )?.use { cursor ->
            val nameIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            val numberIndex = cursor.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            while (cursor.moveToNext()) {
                val name = cursor.getString(nameIndex)?.takeIf { it.isNotBlank() }
                    ?: continue
                val number = cursor.getString(numberIndex)
                    ?: continue
                val normalized = PhoneNumberUtils.normalizeNumber(number)

                if (normalized.isBlank()) {
                    continue
                }

                exactMatches[normalized] = name
                if (normalized.length >= 10) {
                    lastTenCandidates
                        .getOrPut(normalized.takeLast(10)) { mutableSetOf() }
                        .add(name)
                }
            }

            val result = exactMatches.toMutableMap()
            lastTenCandidates.forEach { (lastTen, names) ->
                if (names.size == 1 && !result.containsKey(lastTen)) {
                    result[lastTen] = names.first()
                }
            }
            result
        } ?: emptyMap()
    } catch (_: SecurityException) {
        emptyMap()
    }
}

private fun startOfDayMillis(calendar: Calendar): Long {
    return (calendar.clone() as Calendar).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}

private fun addDays(millis: Long, days: Int): Long {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = millis
        add(Calendar.DAY_OF_MONTH, days)
    }
    return startOfDayMillis(calendar)
}

private fun formatTime(millis: Long): String {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(millis))
}