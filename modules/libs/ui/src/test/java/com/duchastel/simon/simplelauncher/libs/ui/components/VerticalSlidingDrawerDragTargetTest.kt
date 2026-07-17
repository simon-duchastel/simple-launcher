package com.duchastel.simon.simplelauncher.libs.ui.components

import org.junit.Assert.assertEquals
import org.junit.Test

class VerticalSlidingDrawerDragTargetTest {

    @Test
    fun `opening drag up past threshold expands`() {
        val target = computeDragTarget(
            opening = true,
            velocity = 0f,
            dragDistance = 100f,
            velocityThreshold = 125f,
            positionalThreshold = 56f,
        )

        assertEquals(DragAnchors.Expanded, target)
    }

    @Test
    fun `opening drag up below threshold collapses`() {
        val target = computeDragTarget(
            opening = true,
            velocity = 0f,
            dragDistance = 20f,
            velocityThreshold = 125f,
            positionalThreshold = 56f,
        )

        assertEquals(DragAnchors.Hidden, target)
    }

    @Test
    fun `closing drag down past threshold collapses`() {
        val target = computeDragTarget(
            opening = false,
            velocity = 0f,
            dragDistance = -100f,
            velocityThreshold = 125f,
            positionalThreshold = 56f,
        )

        assertEquals(DragAnchors.Hidden, target)
    }

    @Test
    fun `closing drag down below threshold stays expanded`() {
        val target = computeDragTarget(
            opening = false,
            velocity = 0f,
            dragDistance = -20f,
            velocityThreshold = 125f,
            positionalThreshold = 56f,
        )

        assertEquals(DragAnchors.Expanded, target)
    }
}
