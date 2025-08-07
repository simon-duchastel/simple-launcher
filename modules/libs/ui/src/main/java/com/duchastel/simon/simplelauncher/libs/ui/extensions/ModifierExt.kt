package com.duchastel.simon.simplelauncher.libs.ui.extensions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import java.time.Duration

/**
 * A modifier that provides a bounce effect when clicked. Inherently includes [clickable].
 */
fun Modifier.bounceClickable(
    onLongClick: ((Duration) -> Unit)? = null,
    longPressTimeout: Long = 500L,
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
) = composed {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
        label = "bounce_animation"
    )

    Modifier
        .scale(scale)
        .pointerInput(onDoubleClick, onClick) {
            forEachGesture {
                awaitPointerEventScope {
                    awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    val downTime = System.currentTimeMillis()

                    val up = waitForUpOrCancellation()

                    isPressed = false
                    val upTime = System.currentTimeMillis()

                    if (up != null) {
                        val duration = upTime - downTime
                        if (duration >= longPressTimeout) {
                            onLongClick?.invoke(Duration.ofMillis(duration))
                        } else {
                            onClick()
                        }
                    }
                }
            },
            onTap = { onClick() },
            onDoubleTap = { onDoubleClick?.invoke() }
            )
            }
        }
}
