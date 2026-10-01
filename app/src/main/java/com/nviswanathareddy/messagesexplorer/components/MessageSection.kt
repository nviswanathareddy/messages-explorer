package com.nviswanathareddy.messagesexplorer.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nviswanathareddy.messagesexplorer.dialogs.MonthYearPickerDialog
import com.nviswanathareddy.messagesexplorer.dialogs.SortDialog
import com.nviswanathareddy.messagesexplorer.model.MessageSort
import com.nviswanathareddy.messagesexplorer.model.SmsMessage
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun MessageSection(
    filteredMessages: List<SmsMessage>,
    listState: LazyListState,
    sortOption: MessageSort,
    onSortChange: (MessageSort) -> Unit,
    fontScale: Float,
    darkMode: Boolean,
    expandedMessageId: Long?,
    onMessageClick: (Long) -> Unit,
    hasSmsPermission: Boolean,
    onPermissionRequired: @Composable () -> Unit,
    emptyContent: @Composable () -> Unit,
    palette: AppPalette,
    enableMonthPicker: Boolean,
    showDate: Boolean,
    messageMonths: Set<Pair<Int, Int>>,
    onTodayClick: () -> Unit = {},
) {
  var sortDialogOpen by remember {
    mutableStateOf(false)
  }
  var monthPickerOpen by remember {
    mutableStateOf(false)
  }
  var selectedMonthMillis by remember {
    mutableStateOf<Long?>(null)
  }
  val selectedMonthLabel =
      remember(selectedMonthMillis) {
        selectedMonthMillis?.let {
          SimpleDateFormat(
                  "MMMM yyyy",
                  Locale.getDefault(),
              )
              .format(Date(it))
        } ?: "All Months"
      }
  val displayedMessages =
      if (enableMonthPicker && selectedMonthMillis != null) {
        val selectedCalendar =
            Calendar.getInstance().apply {
              timeInMillis = selectedMonthMillis!!
            }
        filteredMessages.filter { message ->
          val messageCalendar =
              Calendar.getInstance().apply {
                timeInMillis = message.timestamp
              }
          messageCalendar.get(Calendar.YEAR) == selectedCalendar.get(Calendar.YEAR) &&
              messageCalendar.get(Calendar.MONTH) == selectedCalendar.get(Calendar.MONTH)
        }
      } else {
        filteredMessages
      }
  Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(if (enableMonthPicker) 4.dp else 12.dp))
    MessageFilterBar(
        messageCount = displayedMessages.size,
        centerText =
            if (enableMonthPicker) {
              selectedMonthLabel
            } else {
              "Today"
            },
        palette = palette,
        fontScale = fontScale,
        sortOption = sortOption,
        onCenterClick = {
          if (enableMonthPicker) {
            monthPickerOpen = true
          } else {
            onTodayClick()
          }
        },
        onSortClick = {
          sortDialogOpen = true
        },
    )
    if (!hasSmsPermission) {
      onPermissionRequired()
    } else if (displayedMessages.isEmpty()) {
      Spacer(modifier = Modifier.height(4.dp))
      emptyContent()
    } else {
      Spacer(modifier = Modifier.height(12.dp))
      LazyColumn(
          state = listState,
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp),
          contentPadding = PaddingValues(bottom = 90.dp),
      ) {
        items(
            items = displayedMessages,
            key = { message -> message.id },
            contentType = { "message" },
        ) { message ->
          MessageCard(
              message = message,
              fontScale = fontScale,
              darkMode = darkMode,
              expanded = expandedMessageId == message.id,
              showDate = showDate,
              onClick = {
                onMessageClick(message.id)
              },
          )
        }
      }
    }
  }
  if (enableMonthPicker && monthPickerOpen) {
    MonthYearPickerDialog(
        selectedMonthMillis = selectedMonthMillis,
        messageMonths = messageMonths,
        fontScale = fontScale,
        darkMode = darkMode,
        onDismiss = {
          monthPickerOpen = false
        },
        onSelectMonth = { monthMillis ->
          selectedMonthMillis = monthMillis
          monthPickerOpen = false
        },
    )
  }
  if (sortDialogOpen) {
    SortDialog(
        sortOption = sortOption,
        fontScale = fontScale,
        darkMode = darkMode,
        onDismiss = {
          sortDialogOpen = false
        },
        onApply = { selectedSort ->
          onSortChange(selectedSort)
          sortDialogOpen = false
        },
    )
  }
}
