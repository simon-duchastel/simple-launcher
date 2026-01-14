package com.duchastel.simon.simplelauncher.libs.ui.extensions

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

/**
 * A modifier that provides a bounce effect when clicked, making this Composable [clickable].
 */
@OptIn(ExperimentalTime::class)
fun Modifier.bounceClickable(
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: ((LongClickScope) -> Unit)? = null,
    bounceClickableScope: BounceClickableScope? = null,
) = composed {
    val bounceClickableScope = bounceClickableScope ?: remember { BounceClickableScopeImpl() }
    val isPressed by bounceClickableScope.collectIsPressedAsState()
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
                    bounceClickableScope.setIsPressed(true)
                    coroutineScope {
                        val successAsync = async { tryAwaitRelease() }
                        var longClick = false
                        val longPressJob = launch {
                            delay(500.milliseconds)
                            longClick = true
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
                        bounceClickableScope.setIsPressed(false)
                    }
                },
                onTap = { onClick() },
                onDoubleTap = { onDoubleClick?.invoke() },
                onLongPress = {
                    // we don't use this lambda (we register our onLongClick lambda in the `onPress`
                    // block so we know the duration of the long press). We still need to register
                    // it to prevent `onTap` from being called on long presses.
                },
            )
        }
}

/**
 * A simpler version of the [bounceClickable] modifier where the [onLongClick] simply
 * provides the duration of the longClick rather than having the consumer manually calculate
 * it from the [LongClickScope].
 */
@OptIn(ExperimentalTime::class)
fun Modifier.bounceClickable(
    onClick: () -> Unit,
    onDoubleClick: (() -> Unit)? = null,
    onLongClick: (suspend (Duration) -> Unit)? = null,
) {
    this.bounceClickable(
        onClick = onClick,
        onDoubleClick = onDoubleClick,
        onLongClick = { longClickScope: LongClickScope ->
            suspend {
                val start = Clock.System.now()
                val success = longClickScope.tryAwaitRelease()
                val end = Clock.System.now()
                onLongClick?.let { lambda ->
                    if (success) {
                        lambda(end - start)
                    }
                }
            }
        }
    )
}

sealed interface BounceClickableScope {
    @Composable
    fun collectIsPressedAsState(): State<Boolean>
}

private fun BounceClickableScope.setIsPressed(value: Boolean) {
    when (this) {
        is BounceClickableScopeImpl -> {
            isPressed.value = value
        }
    }
}

private class BounceClickableScopeImpl: BounceClickableScope {
    var isPressed = mutableStateOf(false)

    @Composable
    override fun collectIsPressedAsState(): State<Boolean> = isPressed
}

@Composable
fun rememberBounceClickableScope(): BounceClickableScope = remember {
    BounceClickableScopeImpl()
}

interface LongClickScope {
    suspend fun tryAwaitRelease(): Boolean
}
