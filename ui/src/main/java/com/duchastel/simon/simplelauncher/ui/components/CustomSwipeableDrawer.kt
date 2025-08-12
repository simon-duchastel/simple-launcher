package com.duchastel.simon.simplelauncher.ui.components

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

enum class DragAnchors {
    Hidden,
    Expanded
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomSwipeableDrawer(
    drawerContent: @Composable () -> Unit,
    mainContent: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val density = LocalDensity.current
        val scope: BoxWithConstraintsScope = this // necessary for lint
        val anchors = DraggableAnchors {
            DragAnchors.Hidden at scope.constraints.maxHeight.toFloat()
            DragAnchors.Expanded at 0f
        }
        val state: AnchoredDraggableState<DragAnchors> = remember {
            AnchoredDraggableState<DragAnchors>(
                initialValue = DragAnchors.Hidden,
                positionalThreshold = { distance: Float -> distance * 0.5f },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                snapAnimationSpec = spring<Float>() as AnimationSpec<Float>,
                decayAnimationSpec = exponentialDecay(),
            ).apply {
                updateAnchors(anchors, DragAnchors.Hidden)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .anchoredDraggable(state = state, orientation = Orientation.Vertical)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                mainContent()
            }
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            0,
                            state.offset.roundToInt()
                        )
                    }
            ) {
                drawerContent()
            }
        }
    }
}

