package com.duchastel.simon.simplelauncher.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
        val interactionSource = remember { MutableInteractionSource() }

        Box(
            modifier = modifier
                .anchoredDraggable(
                    state = state,
                    orientation = Orientation.Vertical,
                    overscrollEffect = rememberOverscrollEffect(),
                    reverseDirection = false,
                    interactionSource = interactionSource
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                content()
            }
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background)
                    .offset {
                        IntOffset(
                            0,
                            state.offset.roundToInt()
                        )
                    }.anchoredDraggable(
                        state = state,
                        orientation = Orientation.Vertical,
                        reverseDirection = false,
                        interactionSource = interactionSource
                    )
            ) {
                drawerContent()
            }
        }
    }
}
