package com.duchastel.simon.simplelauncher.libs.ui.extensions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

/**
 * A modifier that provides a bounce effect when clicked. Inherently includes [clickable].
 */
@OptIn(ExperimentalTime::class)
fun Modifier.bounceClickable(
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: ((LongClickScope) -> Unit)? = null,
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "bounce_animation"
    )

    this
        .scale(scale)
        .pointerInput(onDoubleClick, onClick) {
            detectTapGestures(
                onPress = {
                    isPressed = true
                    coroutineScope {
                        val successAsync = async { tryAwaitRelease() }
                        val longPressJob = launch {
                            delay(500.milliseconds)
                            onLongClick?.invoke(
                                object : LongClickScope {
                                    override suspend fun tryAwaitRelease(): Boolean {
                                        return successAsync.await()
                                    }
                                }
                            )
                        }
                        successAsync.await()
                        longPressJob.cancel()
                        isPressed = false
                    }
                },
                onTap = { onClick() },
                onDoubleTap = { onDoubleClick?.invoke() }
            )
        }
}

interface LongClickScope {
    suspend fun tryAwaitRelease(): Boolean
}
