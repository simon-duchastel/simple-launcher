package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.roundToInt

enum class DragAnchors {
    Hidden,
    Expanded,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSlidingDrawer(
    modifier: Modifier = Modifier,
    expandedTopPadding: Dp = 64.dp,
    scrollPropagationThreshold: Dp = 8.dp,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope: BoxWithConstraintsScope = this // necessary for lint
        val density = LocalDensity.current
        val expandedOffsetPx = with(density) { expandedTopPadding.toPx() }
        val thresholdPx = with(density) { scrollPropagationThreshold.toPx() }
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at scope.constraints.maxHeight.toFloat()
            DragAnchors.Expanded at expandedOffsetPx
        }
        val state: AnchoredDraggableState<DragAnchors> = remember(expandedTopPadding) {
            AnchoredDraggableState<DragAnchors>(
                initialValue = DragAnchors.Hidden,
                anchors = anchors,
            )
        }
        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(state)
        val interactionSource = remember { MutableInteractionSource() }
        val dragHandleInteractionSource = remember { MutableInteractionSource() }
        val nestedScrollConnection = remember(thresholdPx) {
            object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    // Don't propagate to drawer if child consumed any scroll,
                    // or if available is below threshold (treat as noise)
                    if (consumed.y != 0f || abs(available.y) < thresholdPx) {
                        return Offset(x = available.x, y = available.y)
                    }
                    // Child at boundary with significant overscroll - propagate to drawer
                    val additionalConsumedY = state.dispatchRawDelta(available.y)
                    val remainingAvailableY = available.y - additionalConsumedY
                    return Offset(x = available.x, y = remainingAvailableY)
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    // Don't propagate fling if child consumed any velocity,
                    // or if available is below threshold
                    if (consumed.y != 0f || abs(available.y) < thresholdPx * 10) {
                        return Velocity(x = 0f, y = available.y)
                    }
                    var remainingAvailableVelocityY = available.y
                    state.anchoredDrag {
                        val scrollFlingScope =
                            object : ScrollScope {
                                override fun scrollBy(pixels: Float): Float {
                                    dragTo(state.offset + pixels)
                                    return pixels
                                }
                            }
                        remainingAvailableVelocityY = with(flingBehavior) {
                            scrollFlingScope.performFling(available.y)
                        }
                    }
                    return Velocity(x = 0f, y = remainingAvailableVelocityY)
                }
            }
        }

        Box(
            modifier = modifier.nestedScroll(nestedScrollConnection)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .anchoredDraggable(
                        state = state,
                        orientation = Orientation.Vertical,
                        flingBehavior = flingBehavior,
                        reverseDirection = false,
                        interactionSource = interactionSource,
                    )
            ) {
                content()
            }
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, state.offset.roundToInt()) },
                shape = BottomSheetDefaults.ExpandedShape,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .anchoredDraggable(
                                state = state,
                                orientation = Orientation.Vertical,
                                flingBehavior = flingBehavior,
                                reverseDirection = false,
                                interactionSource = dragHandleInteractionSource,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        BottomSheetDefaults.DragHandle()
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        drawerContent()
                    }
                }
            }
        }
    }
}
