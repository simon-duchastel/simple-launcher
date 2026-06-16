package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
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
        val scope: BoxWithConstraintsScope = this // necessary for lint
        val density = LocalDensity.current
        val expandedOffsetPx = with(density) { expandedTopPadding.toPx() }
        val maxHeightPx = scope.constraints.maxHeight.toFloat()
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at maxHeightPx
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
        val coroutineScope = rememberCoroutineScope()

        // Nested scroll connection on the sheet surface.
        // Copies Material3 ConsumeSwipeWithinBottomSheetBoundsNestedScrollConnection pattern:
        // - onPreScroll: intercept upward scroll (pulling down at top of list) and move sheet
        // - onPostScroll: intercept downward overscroll and move sheet
        // - onPreFling: consume downward fling when sheet is not at min anchor
        // - onPostFling: settle sheet with remaining velocity
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val delta = available.y
                    return if (delta < 0f && source == NestedScrollSource.UserInput) {
                        state.dispatchRawDelta(delta).let {
                            Offset(x = 0f, y = it)
                        }
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
                        state.dispatchRawDelta(available.y).let {
                            Offset(x = 0f, y = it)
                        }
                    } else {
                        Offset.Zero
                    }
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    val toFling = available.y
                    val currentOffset = state.offset
                    val minAnchor = state.anchors.minPosition()
                    return if (toFling < 0f && currentOffset > minAnchor) {
                        // Consuming all — settle animation handles the actual movement
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

        // BUG FIX #2: explicit sheet height.
        // The sheet occupies the screen from expandedOffsetPx down to the bottom.
        // Using fillMaxSize() would make the Surface layout bounds extend the full
        // screen height, but visually offset by expandedOffsetPx. This caused the
        // LazyColumn inside to think it had full-screen height, laying out items
        // that were hidden below the bottom edge. Using explicit height ensures
        // the inner LazyColumn knows its true viewport bounds.
        val sheetHeight = with(density) { (maxHeightPx - expandedOffsetPx).toDp() }

        Box(modifier = modifier.fillMaxSize()) {
            // Background content (homepage)
            Box(modifier = Modifier.fillMaxSize()) {
                content()
            }

            // Full-screen transparent overlay that captures swipe-up ONLY when the
            // drawer is fully hidden. Uses custom pointerInput that always consumes
            // events during the drag, preventing children from starting a competing
            // gesture. This is the "swipe up from anywhere on the wallpaper" behavior.
            if (state.offset >= maxHeightPx - 1f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(state) {
                            awaitEachGesture {
                                val down = awaitFirstDown(requireUnconsumed = false)
                                val slop = viewConfiguration.touchSlop
                                var totalDelta = 0f

                                // Slop detection — only upward motion opens drawer
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

                                // Drag phase — consume ALL events so LazyColumn can't start
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

                                // Settle with velocity-aware snap
                                val velocity = velocityTracker.calculateVelocity().y
                                val currentOffset = state.offset
                                val hiddenOffset = state.anchors.positionOf(DragAnchors.Hidden)
                                val expandedOffset = state.anchors.positionOf(DragAnchors.Expanded)

                                val target = when {
                                    velocity > 1000f -> DragAnchors.Hidden
                                    velocity < -1000f -> DragAnchors.Expanded
                                    currentOffset > (hiddenOffset + expandedOffset) / 2 ->
                                        DragAnchors.Hidden
                                    else -> DragAnchors.Expanded
                                }

                                coroutineScope.launch {
                                    val anim = Animatable(
                                        currentOffset,
                                        Float.VectorConverter
                                    )
                                    anim.animateTo(
                                        targetValue = state.anchors.positionOf(target),
                                        initialVelocity = velocity,
                                    ) {
                                        state.dispatchRawDelta(value - state.offset)
                                    }
                                }
                            }
                        }
                )
            }

            // Sheet surface — anchoredDraggable on the full Surface (not just handle).
            // This matches Material3 StandardBottomSheet architecture:
            // - anchoredDraggable on the Surface handles drag-down-to-close
            // - LazyColumn inside coordinates via nestedScroll (list scrolls normally,
            //   overscroll at top propagates to close the sheet)
            // At PointerEventPass.Main, child (LazyColumn.scrollable) goes BEFORE parent
            // (Surface.anchoredDraggable), so the list wins the slop race when scrolling.
            // When pulling down at the top of the list, overscroll goes through nestedScroll
            // and moves the sheet. The drag handle area (above the list) is also draggable.
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
