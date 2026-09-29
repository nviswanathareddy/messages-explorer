package com.nviswanathareddy.messagesexplorer.dialogs

import android.annotation.SuppressLint
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.nviswanathareddy.messagesexplorer.utils.AppPalette
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val DialogWidthFraction = 0.90f
private const val DialogTopPositionFraction = 0.20f
private const val DialogCornerRadius = 24f
private const val DialogShadowElevation = 12f
private const val DialogScrimAlpha = 0.32f
private const val DialogAnimationDuration = 220
private const val DialogInitialScale = 0.94f
private const val DialogExitScale = 0.94f
private const val DialogBlurRadius = 20

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AppDialog(
    palette: AppPalette,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit
) {
    var visible by remember {
        mutableStateOf(false)
    }

    var dismissRequested by remember {
        mutableStateOf(false)
    }

    val configuration =
        LocalConfiguration.current

    val screenWidth =
        configuration.screenWidthDp.dp

    val screenHeight =
        configuration.screenHeightDp.dp

    val dialogWidth =
        screenWidth * DialogWidthFraction

    val topPadding =
        screenHeight * DialogTopPositionFraction

    fun requestDismiss() {
        if (dismissRequested) {
            return
        }

        dismissRequested = true
        visible = false
    }

    LaunchedEffect(Unit) {
        visible = true
    }

    LaunchedEffect(dismissRequested) {
        if (dismissRequested) {
            delay(
                DialogAnimationDuration.toLong()
                    .milliseconds
            )

            onDismiss()
        }
    }

    if (dismissOnBackPress) {
        BackHandler {
            requestDismiss()
        }
    }

    Dialog(
        onDismissRequest = {
            if (dismissOnClickOutside) {
                requestDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside =
                dismissOnClickOutside,
            dismissOnBackPress = false
        )
    ) {
        val dialogView =
            LocalView.current

        val dialogWindow =
            (
                    dialogView.parent
                            as? DialogWindowProvider
                    )?.window

        DisposableEffect(dialogWindow) {
            if (
                dialogWindow != null &&
                Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.S
            ) {
                val originalFlags =
                    dialogWindow.attributes.flags

                val originalBlurRadius =
                    dialogWindow.attributes
                        .blurBehindRadius

                dialogWindow.addFlags(
                    WindowManager.LayoutParams
                        .FLAG_BLUR_BEHIND
                )

                val attributes =
                    dialogWindow.attributes

                attributes.blurBehindRadius =
                    DialogBlurRadius

                dialogWindow.attributes =
                    attributes

                onDispose {
                    val restoreAttributes =
                        dialogWindow.attributes

                    restoreAttributes.flags =
                        originalFlags

                    restoreAttributes.blurBehindRadius =
                        originalBlurRadius

                    dialogWindow.attributes =
                        restoreAttributes
                }
            } else {
                onDispose {}
            }
        }

        Box(
            modifier =
                Modifier.fillMaxSize()
        ) {

            // -----------------------------------------------------
            // SCRIM
            // -----------------------------------------------------

            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(
                                alpha =
                                    DialogScrimAlpha
                            )
                        )
            )

            // -----------------------------------------------------
            // DIALOG CONTENT
            // -----------------------------------------------------

            AnimatedVisibility(
                visible = visible,
                modifier =
                    Modifier
                        .align(
                            Alignment.TopCenter
                        )
                        .padding(
                            top = topPadding
                        ),
                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis =
                                    DialogAnimationDuration,
                                easing =
                                    FastOutSlowInEasing
                            )
                    ) +
                            scaleIn(
                                initialScale =
                                    DialogInitialScale,
                                animationSpec =
                                    tween(
                                        durationMillis =
                                            DialogAnimationDuration,
                                        easing =
                                            FastOutSlowInEasing
                                    )
                            ),
                exit =
                    fadeOut(
                        animationSpec =
                            tween(
                                durationMillis =
                                    DialogAnimationDuration,
                                easing =
                                    FastOutSlowInEasing
                            )
                    ) +
                            scaleOut(
                                targetScale =
                                    DialogExitScale,
                                animationSpec =
                                    tween(
                                        durationMillis =
                                            DialogAnimationDuration,
                                        easing =
                                            FastOutSlowInEasing
                                    )
                            )
            ) {
                Surface(
                    modifier =
                        modifier.width(
                            dialogWidth
                        ),
                    shape =
                        RoundedCornerShape(
                            DialogCornerRadius.dp
                        ),
                    color =
                        palette.surface,
                    shadowElevation =
                        DialogShadowElevation.dp
                ) {
                    Column {
                        content(
                            ::requestDismiss
                        )
                    }
                }
            }
        }
    }
}