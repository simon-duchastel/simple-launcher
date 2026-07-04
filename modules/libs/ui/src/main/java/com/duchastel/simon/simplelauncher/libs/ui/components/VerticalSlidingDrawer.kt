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
import androidx.compose.foundation.gestures.animateTo
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
import androidx.compose.foundation.layout.width
import androidx.activity.BackEventCompat
import androidx.activity.compose.PredictiveBackHandler
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.roundToInt

enum class DragAnchors {
    Hidden,
    Expanded,
}

@Stable
class VerticalSlidingDrawerState(
    initialValue: DragAnchors = DragAnchors.Hidden,
) {
    internal val anchoredDraggableState: AnchoredDraggableState<DragAnchors> =
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = DraggableAnchors {
                DragAnchors.Hidden at 0f
                DragAnchors.Expanded at 0f
            },
        )

    val currentValue: DragAnchors
        get() = anchoredDraggableState.currentValue

    val targetValue: DragAnchors
        get() = anchoredDraggableState.targetValue

    suspend fun collapse() {
        anchoredDraggableState.animateTo(DragAnchors.Hidden)
    }

    val progress: Float by derivedStateOf(structuralEqualityPolicy()) {
        val offset = anchoredDraggableState.offset
        if (offset.isNaN()) {
            0f
        } else {
            val hiddenOffset = anchoredDraggableState.anchors.positionOf(DragAnchors.Hidden)
            val expandedOffset = anchoredDraggableState.anchors.positionOf(DragAnchors.Expanded)
            if (hiddenOffset.isNaN() || expandedOffset.isNaN()) {
                0f
            } else {
                val range = hiddenOffset - expandedOffset
                if (range == 0f) 0f else ((hiddenOffset - offset) / range).coerceIn(0f, 1f)
            }
        }
    }
}

@Composable
fun rememberVerticalSlidingDrawerState(
    initialValue: DragAnchors = DragAnchors.Hidden,
): VerticalSlidingDrawerState = remember {
    VerticalSlidingDrawerState(initialValue = initialValue)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSlidingDrawer(
    state: VerticalSlidingDrawerState = rememberVerticalSlidingDrawerState(),
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope: BoxWithConstraintsScope = this
        val density = LocalDensity.current
        val maxWidthDp = with(density) { scope.constraints.maxWidth.toDp() }
        val isLargeScreen = maxWidthDp > BottomSheetDefaults.SheetMaxWidth
        val expandedTopPadding = if (isLargeScreen) 56.dp else 72.dp
        val expandedOffsetPx = with(density) { expandedTopPadding.toPx() }
        val maxHeightPx = scope.constraints.maxHeight.toFloat()
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at maxHeightPx
            DragAnchors.Expanded at expandedOffsetPx
        }
        val drawerState = state.anchoredDraggableState
        SideEffect {
            if (drawerState.anchors != anchors) {
                drawerState.updateAnchors(anchors)
            }
        }
        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(drawerState)
        val interactionSource = remember { MutableInteractionSource() }
        val coroutineScope = rememberCoroutineScope()

        val isExpanded by remember { derivedStateOf { state.currentValue == DragAnchors.Expanded } }
        PredictiveBackHandler(enabled = isExpanded) { progress ->
            val hiddenOffset = maxHeightPx
            val expandedOffset = expandedOffsetPx
            val peekFraction = 0.3f

            try {
                progress.collect { event: BackEventCompat ->
                    val targetOffset = expandedOffset +
                        (hiddenOffset - expandedOffset) * event.progress * peekFraction
                    val delta = targetOffset - drawerState.offset
                    if (!delta.isNaN()) {
                        drawerState.dispatchRawDelta(delta)
                    }
                }
                drawerState.animateTo(DragAnchors.Hidden)
            } catch (e: CancellationException) {
                withContext(NonCancellable) {
                    drawerState.animateTo(DragAnchors.Expanded)
                }
                throw e
            }
        }

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
                        Offset(x = 0f, y = drawerState.dispatchRawDelta(delta))
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
                        Offset(x = 0f, y = drawerState.dispatchRawDelta(available.y))
                    } else {
                        Offset.Zero
                    }
                }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    val toFling = available.y
                    return if (toFling < 0f && drawerState.offset > drawerState.anchors.minPosition()) {
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
                    drawerState.anchoredDrag {
                        val scrollFlingScope =
                            object : ScrollScope {
                                override fun scrollBy(pixels: Float): Float {
                                    dragTo(drawerState.offset + pixels)
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
                    .pointerInput(drawerState) {
                        awaitEachGesture {
                            val down = awaitFirstDown(requireUnconsumed = false)
                            if (drawerState.offset < maxHeightPx - 1f) return@awaitEachGesture

                            val slop = viewConfiguration.touchSlop
                            val startOffset = drawerState.offset
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
                                drawerState.dispatchRawDelta(delta)
                            }

                            val velocity = velocityTracker.calculateVelocity().y
                            val currentOffset = drawerState.offset
                            val dragDistance = startOffset - currentOffset

                            val target = when {
                                velocity > velocityThreshold -> DragAnchors.Hidden
                                velocity < -velocityThreshold -> DragAnchors.Expanded
                                dragDistance > positionalThreshold -> DragAnchors.Expanded
                                else -> DragAnchors.Hidden
                            }

                            coroutineScope.launch {
                                drawerState.anchoredDrag {
                                    val anim = Animatable(
                                        currentOffset,
                                        Float.VectorConverter
                                    )
                                    anim.animateTo(
                                        targetValue = drawerState.anchors.positionOf(target),
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

            val drawerAlpha = state.progress

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Surface(
                    modifier = Modifier
                        .then(if (isLargeScreen) Modifier.width(BottomSheetDefaults.SheetMaxWidth) else Modifier.fillMaxWidth())
                        .height(sheetHeight)
                        .offset { IntOffset(0, drawerState.offset.roundToInt()) }
                        .alpha(drawerAlpha)
                        .anchoredDraggable(
                            state = drawerState,
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
}
