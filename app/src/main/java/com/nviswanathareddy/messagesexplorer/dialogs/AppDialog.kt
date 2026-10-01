package com.nviswanathareddy.messagesexplorer.dialogs
import android.annotation.SuppressLint
import android.os.Build
import android.view.WindowManager
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.runtime.remember
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
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.delay

private const val DialogWidthFraction = 0.90f
private const val DialogTopPositionFraction = 0.1f
private const val DialogCornerRadius = 12f
private const val DialogShadowElevation = 12f
private const val DialogScrimAlpha = 0.10f
private const val DialogAnimationDuration = 150
private const val DialogExitAnimationDuration = 100
private const val DialogInitialScale = 0.98f
private const val DialogExitScale = 0.98f
private const val DialogBlurRadius = 12

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun AppDialog(
    palette: AppPalette,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissOnClickOutside: Boolean = true,
    dismissOnBackPress: Boolean = true,
    topPositionFraction: Float = DialogTopPositionFraction,
    content: @Composable ColumnScope.(dismiss: () -> Unit) -> Unit,
) {
    val visible = remember {
        MutableTransitionState(false)
    }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val dialogWidth = screenWidth * DialogWidthFraction
    val topPadding = screenHeight * topPositionFraction
    fun requestDismiss() {
        if (visible.targetState.not()) {
            return
        }
        visible.targetState = false
    }
    LaunchedEffect(Unit) {
        visible.targetState = true
    }
    LaunchedEffect(visible.targetState) {
        if (!visible.targetState) {
            delay(DialogExitAnimationDuration.toLong().milliseconds)
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
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnClickOutside = dismissOnClickOutside,
                dismissOnBackPress = false,
            ),
    ) {
        val dialogView = LocalView.current
        val dialogWindow = (dialogView.parent as? DialogWindowProvider)?.window
        DisposableEffect(dialogWindow) {
            if (dialogWindow != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val originalFlags = dialogWindow.attributes.flags
                val originalBlurRadius = dialogWindow.attributes.blurBehindRadius
                dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
                val attributes = dialogWindow.attributes
                attributes.blurBehindRadius = DialogBlurRadius
                dialogWindow.attributes = attributes
                onDispose {
                    val restoreAttributes = dialogWindow.attributes
                    restoreAttributes.flags = originalFlags
                    restoreAttributes.blurBehindRadius = originalBlurRadius
                    dialogWindow.attributes = restoreAttributes
                }
            } else {
                onDispose {}
            }
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = DialogScrimAlpha))
            )
            AnimatedVisibility(
                visibleState = visible,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = topPadding),
                enter =
                    fadeIn(
                        animationSpec =
                            tween(
                                durationMillis = DialogAnimationDuration,
                                easing = FastOutSlowInEasing,
                            )
                    ) +
                            scaleIn(
                                initialScale = DialogInitialScale,
                                animationSpec =
                                    tween(
                                        durationMillis = DialogAnimationDuration,
                                        easing = FastOutSlowInEasing,
                                    ),
                            ),
                exit =
                    fadeOut(
                        animationSpec =
                            tween(
                                durationMillis = DialogExitAnimationDuration,
                                easing = FastOutSlowInEasing,
                            )
                    ) +
                            scaleOut(
                                targetScale = DialogExitScale,
                                animationSpec =
                                    tween(
                                        durationMillis = DialogExitAnimationDuration,
                                        easing = FastOutSlowInEasing,
                                    ),
                            ),
            ) {
                Surface(
                    modifier = modifier.width(dialogWidth),
                    shape = RoundedCornerShape(DialogCornerRadius.dp),
                    color = palette.surface,
                    shadowElevation = DialogShadowElevation.dp,
                ) {
                    Column {
                        content(::requestDismiss)
                    }
                }
            }
        }
    }
}