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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class DragAnchors {
    Hidden,
    Expanded,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSlidingDrawer(
    modifier: Modifier = Modifier,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scope: BoxWithConstraintsScope = this // necessary for lint
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at scope.constraints.maxHeight.toFloat()
            DragAnchors.Expanded at 0f
        }
        val state: AnchoredDraggableState<DragAnchors> = remember {
            AnchoredDraggableState<DragAnchors>(
                initialValue = DragAnchors.Hidden,
                anchors = anchors,
            )
        }
        val flingBehavior = AnchoredDraggableDefaults.flingBehavior(state)
        val interactionSource = remember { MutableInteractionSource() }
        val nestedScrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource
                ): Offset {
                    val additionalConsumedY = state.dispatchRawDelta(available.y)
                    val remainingAvailableY =  available.y - additionalConsumedY
                    return Offset(x = available.x, y = remainingAvailableY)
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity
                ): Velocity {
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
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            ) {
                drawerContent()
            }
        }
    }
}
