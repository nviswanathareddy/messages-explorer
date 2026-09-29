package com.nviswanathareddy.messagesexplorer.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.nviswanathareddy.messagesexplorer.components.DateSection
import com.nviswanathareddy.messagesexplorer.components.EmptyMessagesCard
import com.nviswanathareddy.messagesexplorer.components.FooterSection
import com.nviswanathareddy.messagesexplorer.components.FooterTab
import com.nviswanathareddy.messagesexplorer.components.HeaderSection
import com.nviswanathareddy.messagesexplorer.components.MessageSection
import com.nviswanathareddy.messagesexplorer.components.PermissionCard
import com.nviswanathareddy.messagesexplorer.components.SearchSection
import com.nviswanathareddy.messagesexplorer.data.addDays
import com.nviswanathareddy.messagesexplorer.data.readSmsForDate
import com.nviswanathareddy.messagesexplorer.data.startOfDayMillis
import com.nviswanathareddy.messagesexplorer.dialogs.CustomDatePickerDialog
import com.nviswanathareddy.messagesexplorer.model.MessageSort
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.AppScreenHorizontalPadding
import com.nviswanathareddy.messagesexplorer.utils.DarkPalette
import com.nviswanathareddy.messagesexplorer.utils.LightPalette
import com.nviswanathareddy.messagesexplorer.utils.detectCategory
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarExplorerScreen(
    darkMode: Boolean,
    fontScale: Float,
    refreshTrigger: Int,
    onSettingsClick: () -> Unit,
    onTabSelected: (FooterTab) -> Unit
) {
    val context = LocalContext.current

    val palette = if (darkMode) {
        DarkPalette
    } else {
        LightPalette
    }

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

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            hasSmsPermission =
                permissions[Manifest.permission.READ_SMS] == true ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_SMS
                        ) == PackageManager.PERMISSION_GRANTED

            hasContactsPermission =
                permissions[Manifest.permission.READ_CONTACTS] == true ||
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_CONTACTS
                        ) == PackageManager.PERMISSION_GRANTED
        }

    val contactsPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            hasContactsPermission = it
        }

    LaunchedEffect(
        hasSmsPermission,
        hasContactsPermission
    ) {
        if (hasSmsPermission && !hasContactsPermission) {
            contactsPermissionLauncher.launch(
                Manifest.permission.READ_CONTACTS
            )
        }
    }

    var selectedDateMillis by remember {
        mutableLongStateOf(
            startOfDayMillis(
                Calendar.getInstance()
            )
        )
    }

    var messages by remember {
        mutableStateOf<List<SmsMessage>>(emptyList())
    }

    var searchQuery by remember {
        mutableStateOf("")
    }

    var searchOpen by remember {
        mutableStateOf(false)
    }

    var sortOption by remember {
        mutableStateOf(MessageSort.NEWEST_FIRST)
    }

    var datePickerOpen by remember {
        mutableStateOf(false)
    }

    var expandedMessageId by remember {
        mutableStateOf<Long?>(null)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(
        selectedDateMillis,
        hasSmsPermission,
        hasContactsPermission,
        refreshTrigger
    ) {
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

    LaunchedEffect(
        sortOption,
        selectedDateMillis,
        searchQuery
    ) {
        listState.scrollToItem(0)
        expandedMessageId = null
    }

    val filteredMessages = remember(
        messages,
        searchQuery,
        sortOption
    ) {
        val searchedMessages = if (searchQuery.isBlank()) {
            messages
        } else {
            messages.filter { message ->
                message.sender.contains(
                    searchQuery,
                    ignoreCase = true
                ) ||
                        message.address.contains(
                            searchQuery,
                            ignoreCase = true
                        ) ||
                        message.body.contains(
                            searchQuery,
                            ignoreCase = true
                        ) ||
                        detectCategory(
                            message.sender,
                            message.body
                        ).contains(
                            searchQuery,
                            ignoreCase = true
                        )
            }
        }

        when (sortOption) {
            MessageSort.NEWEST_FIRST ->
                searchedMessages.sortedByDescending {
                    it.timestamp
                }

            MessageSort.OLDEST_FIRST ->
                searchedMessages.sortedBy {
                    it.timestamp
                }

            MessageSort.SENDER_A_TO_Z ->
                searchedMessages.sortedWith(
                    compareBy(
                        {
                            it.sender.lowercase(
                                Locale.getDefault()
                            )
                        },
                        {
                            -it.timestamp
                        }
                    )
                )
        }
    }

    Scaffold(
        containerColor = palette.background,
        bottomBar = {
            Surface(
                modifier = Modifier.navigationBarsPadding()
            ) {
                FooterSection(
                    selectedTab = FooterTab.CALENDAR,
                    onTabSelected = onTabSelected,
                    palette = palette,
                    fontScale = fontScale
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (searchOpen) {
                SearchSection(
                    searchQuery,
                    { searchQuery = it },
                    palette,
                    fontScale
                ) {
                    searchOpen = false
                }
            } else {
                HeaderSection(
                    palette = palette,
                    fontScale = fontScale,
                    onSearchClick = {
                        searchOpen = true
                    },
                    onSettingsClick = {
                        onSettingsClick()
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = AppScreenHorizontalPadding
                    )
            ) {
                DateSection(
                    selectedDateMillis = selectedDateMillis,
                    palette = palette,
                    fontScale = fontScale,
                    onPreviousDay = {
                        selectedDateMillis = addDays(
                            selectedDateMillis,
                            -1
                        )
                    },
                    onNextDay = {
                        selectedDateMillis = addDays(
                            selectedDateMillis,
                            1
                        )
                    },
                    onDateClick = {
                        datePickerOpen = true
                    }
                )

                MessageSection(
                    filteredMessages = filteredMessages,
                    listState = listState,
                    sortOption = sortOption,
                    onSortChange = {
                        sortOption = it
                    },
                    fontScale = fontScale,
                    darkMode = darkMode,
                    expandedMessageId = expandedMessageId,
                    onMessageClick = { id ->
                        expandedMessageId =
                            if (expandedMessageId == id) {
                                null
                            } else {
                                id
                            }
                    },
                    hasSmsPermission = hasSmsPermission,
                    onPermissionRequired = {
                        PermissionCard(
                            fontScale = fontScale,
                            darkMode = darkMode
                        ) {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.READ_CONTACTS
                                )
                            )
                        }
                    },
                    emptyContent = {
                        EmptyMessagesCard(
                            searchQuery = searchQuery,
                            fontScale = fontScale
                        )
                    },
                    palette = palette,
                    enableMonthPicker = false,
                    messageMonths = emptySet(),
                    onTodayClick = {
                        selectedDateMillis =
                            startOfDayMillis(
                                Calendar.getInstance()
                            )
                    }
                )
            }
        }
    }

    if (datePickerOpen) {
        CustomDatePickerDialog(
            selectedDateMillis = selectedDateMillis,
            hasSmsPermission = hasSmsPermission,
            fontScale = fontScale,
            darkMode = darkMode,
            onDismiss = {
                datePickerOpen = false
            },
            onSelectDate = { dateMillis ->
                selectedDateMillis = dateMillis
                datePickerOpen = false
            }
        )
    }
}