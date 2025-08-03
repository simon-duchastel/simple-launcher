package com.duchastel.simon.simplelauncher.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun VerticalSlidingDrawer(
    content: @Composable () -> Unit,
    drawerContent: @Composable () -> Unit
) {
    var drawerOffset by remember { mutableFloatStateOf(0f) }
    val animatedDrawerOffset by animateFloatAsState(
        targetValue = drawerOffset,
        animationSpec = tween(durationMillis = 300), label = "drawerOffsetAnimation"
    )

    val drawerHeightPx = with(LocalDensity.current) { 200.dp.toPx() }

    Box(modifier = Modifier.fillMaxSize()) {
        content()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(x = 0, y = animatedDrawerOffset.roundToInt()) }
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newOffset = animatedDrawerOffset + delta
                        drawerOffset = newOffset.coerceIn(-drawerHeightPx, 0f)
                    },
                    onDragStopped = { velocity ->
                        if (velocity < 0 && animatedDrawerOffset < -drawerHeightPx / 2) {
                            drawerOffset = -drawerHeightPx
                        } else if (velocity > 0 && animatedDrawerOffset > -drawerHeightPx / 2) {
                            drawerOffset = 0f
                        } else if (animatedDrawerOffset < -drawerHeightPx / 2) {
                            drawerOffset = -drawerHeightPx
                        } else {
                            drawerOffset = 0f
                        }
                    }
                )
        ) {
            drawerContent()
        }
    }
}
