package com.nviswanathareddy.messagesexplorer

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import androidx.core.view.WindowCompat
import com.nviswanathareddy.messagesexplorer.components.FooterTab
import com.nviswanathareddy.messagesexplorer.dialogs.SettingsDialog
import com.nviswanathareddy.messagesexplorer.screens.CalendarExplorerScreen
import com.nviswanathareddy.messagesexplorer.screens.MessagesExplorerScreen
import com.nviswanathareddy.messagesexplorer.screens.TransactionsExplorerScreen
import com.nviswanathareddy.messagesexplorer.ui.theme.MessagesExplorerTheme
import com.nviswanathareddy.messagesexplorer.utils.PreferenceDarkMode
import com.nviswanathareddy.messagesexplorer.utils.PreferenceFontScale
import com.nviswanathareddy.messagesexplorer.utils.PreferencesName

private const val PreferenceThemeMode = "preference_theme_mode"
private const val PreferenceTimeFormat = "preference_time_format"

enum class AppThemeMode {
  LIGHT,
  DARK,
  SYSTEM,
}

enum class AppTimeFormat {
  TWELVE_HOUR,
  TWENTY_FOUR_HOUR,
}

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    WindowCompat.getInsetsController(
            window,
            window.decorView,
        )
        .isAppearanceLightStatusBars = true
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
        Context.MODE_PRIVATE,
    )
  }
  val systemDarkMode = isSystemInDarkTheme()

  var themeMode by remember {
    mutableStateOf(loadThemeMode(preferences))
  }

  var fontScale by remember {
    mutableFloatStateOf(
        preferences.getFloat(
            PreferenceFontScale,
            1f,
        )
    )
  }

  var timeFormat by remember {
    mutableStateOf(loadTimeFormat(preferences))
  }

  var selectedTab by remember {
    mutableStateOf(FooterTab.MESSAGES)
  }

  var settingsDialogOpen by remember {
    mutableStateOf(false)
  }

  var refreshTrigger by remember {
    mutableIntStateOf(0)
  }

  val darkMode =
      when (themeMode) {
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
        AppThemeMode.SYSTEM -> systemDarkMode
      }

  val activity = context as? Activity

  SideEffect {
    activity?.window?.let { window ->
      val controller =
          WindowCompat.getInsetsController(
              window,
              window.decorView,
          )
      controller.isAppearanceLightStatusBars = !darkMode
      controller.isAppearanceLightNavigationBars = !darkMode
      controller.systemBarsBehavior =
          androidx.core.view.WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
  }

  MessagesExplorerTheme(darkTheme = darkMode) {
    when (selectedTab) {
      FooterTab.MESSAGES -> {
        MessagesExplorerScreen(
            darkMode = darkMode,
            fontScale = fontScale,
            refreshTrigger = refreshTrigger,
            onSettingsClick = {
              settingsDialogOpen = true
            },
            onTabSelected = { tab ->
              selectedTab = tab
            },
        )
      }

      FooterTab.CALENDAR -> {
        CalendarExplorerScreen(
            darkMode = darkMode,
            fontScale = fontScale,
            refreshTrigger = refreshTrigger,
            onSettingsClick = {
              settingsDialogOpen = true
            },
            onTabSelected = { tab ->
              selectedTab = tab
            },
        )
      }

      FooterTab.TRANSACTIONS -> {
        TransactionsExplorerScreen(
            darkMode = darkMode,
            fontScale = fontScale,
            refreshTrigger = refreshTrigger,
            onSettingsClick = {
              settingsDialogOpen = true
            },
            onTabSelected = { tab ->
              selectedTab = tab
            },
        )
      }
    }

    if (settingsDialogOpen) {
      SettingsDialog(
          darkMode = darkMode,
          fontScale = fontScale,
          themeMode = themeMode,
          timeFormat = timeFormat,
          onThemeModeChange = { newThemeMode ->
            themeMode = newThemeMode
            preferences.edit {
              putString(
                  PreferenceThemeMode,
                  newThemeMode.name,
              )
              putBoolean(
                  PreferenceDarkMode,
                  newThemeMode == AppThemeMode.DARK,
              )
            }
          },
          onFontScaleChange = { newScale ->
            val scale =
                newScale.coerceIn(
                    0.8f,
                    1.4f,
                )
            fontScale = scale
            preferences.edit {
              putFloat(
                  PreferenceFontScale,
                  scale,
              )
            }
          },
          onTimeFormatChange = { newTimeFormat ->
            timeFormat = newTimeFormat
            preferences.edit {
              putString(
                  PreferenceTimeFormat,
                  newTimeFormat.name,
              )
            }
          },
          onRefresh = {
            refreshTrigger++
            settingsDialogOpen = false
          },
          onDismiss = {
            settingsDialogOpen = false
          },
      )
    }
  }
}

private fun loadThemeMode(
    preferences: android.content.SharedPreferences,
): AppThemeMode {
  val savedTheme =
      preferences.getString(
          PreferenceThemeMode,
          null,
      )

  if (savedTheme != null) {
    return try {
      AppThemeMode.valueOf(savedTheme)
    } catch (_: IllegalArgumentException) {
      AppThemeMode.LIGHT
    }
  }

  return if (
      preferences.getBoolean(
          PreferenceDarkMode,
          false,
      )
  ) {
    AppThemeMode.DARK
  } else {
    AppThemeMode.LIGHT
  }
}

private fun loadTimeFormat(
    preferences: android.content.SharedPreferences,
): AppTimeFormat {
  val savedFormat =
      preferences.getString(
          PreferenceTimeFormat,
          null,
      )

  return if (savedFormat != null) {
    try {
      AppTimeFormat.valueOf(savedFormat)
    } catch (_: IllegalArgumentException) {
      AppTimeFormat.TWELVE_HOUR
    }
  } else {
    AppTimeFormat.TWELVE_HOUR
  }
}
