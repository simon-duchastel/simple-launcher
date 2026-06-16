package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.changedToUpIgnoreConsumed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
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
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope: BoxWithConstraintsScope = this
        val density = LocalDensity.current
        val expandedOffsetPx = with(density) { expandedTopPadding.toPx() }
        val maxHeightPx = scope.constraints.maxHeight.toFloat()
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at maxHeightPx
            DragAnchors.Expanded at expandedOffsetPx
        }
        val state: AnchoredDraggableState<DragAnchors> = remember(expandedTopPadding) {
            AnchoredDraggableState(
                initialValue = DragAnchors.Hidden,
                anchors = anchors,
            )
        }
        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(state)
        val interactionSource = remember { MutableInteractionSource() }
        val coroutineScope = rememberCoroutineScope()

        val positionalThreshold = with(density) { 56.dp.toPx() }
        val velocityThreshold = with(density) { 125.dp.toPx() }

        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0f && source == NestedScrollSource.UserInput) {
                        Offset(x = 0f, y = state.dispatchRawDelta(delta))
                    } else {
                        Offset.Zero
                    }
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    return if (source == NestedScrollSource.UserInput) {
                        Offset(x = 0f, y = state.dispatchRawDelta(available.y))
                    } else {
                        Offset.Zero
                    }
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    val toFling = available.y
                    return if (toFling < 0f && state.offset > state.anchors.minPosition()) {
                        available
                    } else {
                        Velocity.Zero
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
                    var remaining = available.y
                    state.anchoredDrag {
                        val scrollFlingScope =
                            object : ScrollScope {
                                override fun scrollBy(pixels: Float): Float {
                                    dragTo(state.offset + pixels)
                                    return pixels
                                }
                            }
                        remaining = with(flingBehavior) {
                            scrollFlingScope.performFling(available.y)
                        }
                    }
                    return Velocity(x = 0f, y = remaining)
                }
            }
        }

        // fillMaxSize() + offset makes the Surface layout bounds extend past the
        // screen bottom, so the inner LazyColumn lays out items in the clipped area.
        val sheetHeight = with(density) { (maxHeightPx - expandedOffsetPx).toDp() }

        Box(modifier = modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }

            // Raw pointerInput so the gesture is never stolen by the LazyColumn
            // when the finger strays onto it mid-drag.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(state) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            if (state.offset < maxHeightPx - 1f) return@awaitEachGesture

                            val slop = viewConfiguration.touchSlop
                            val startOffset = state.offset
                            var totalDelta = 0f

                            var slopExceeded = false
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.find { it.id == down.id }
                                    ?: break
                                if (change.changedToUpIgnoreConsumed()) break

                                totalDelta += change.positionChange().y
                                if (totalDelta < -slop) {
                                    change.consume()
                                    slopExceeded = true
                                    break
                                }
                            }

                            if (!slopExceeded) return@awaitEachGesture

                            val velocityTracker = VelocityTracker()
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.find { it.id == down.id }
                                    ?: break
                                if (change.changedToUpIgnoreConsumed()) break

                                val delta = change.positionChange().y
                                change.consume()
                                velocityTracker.addPosition(
                                    change.uptimeMillis,
                                    change.position
                                )
                                state.dispatchRawDelta(delta)
                            }

                            val velocity = velocityTracker.calculateVelocity().y
                            val currentOffset = state.offset
                            val dragDistance = startOffset - currentOffset

                            val target = when {
                                velocity > velocityThreshold -> DragAnchors.Hidden
                                velocity < -velocityThreshold -> DragAnchors.Expanded
                                dragDistance > positionalThreshold -> DragAnchors.Expanded
                                else -> DragAnchors.Hidden
                            }

                            coroutineScope.launch {
                                state.anchoredDrag {
                                    val anim = Animatable(
                                        currentOffset,
                                        Float.VectorConverter
                                    )
                                    anim.animateTo(
                                        targetValue = state.anchors.positionOf(target),
                                        animationSpec = tween(
                                            durationMillis = 300,
                                            easing = FastOutSlowInEasing
                                        ),
                                        initialVelocity = velocity,
                                    ) {
                                        dragTo(value)
                                    }
                                }
                            }
                        }
                    }
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .offset { IntOffset(0, state.offset.roundToInt()) }
                    .anchoredDraggable(
                        state = state,
                        orientation = Orientation.Vertical,
                        flingBehavior = flingBehavior,
                        reverseDirection = false,
                        interactionSource = interactionSource,
                    )
                    .nestedScroll(nestedScrollConnection),
                shape = BottomSheetDefaults.ExpandedShape,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    BottomSheetDefaults.DragHandle(
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        drawerContent()
                    }
                }
            }
        }
    }
}
