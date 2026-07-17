package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.runtime.saveable.SaverScope
import org.junit.Assert.assertEquals
import org.junit.Test

class VerticalSlidingDrawerStateTest {

    private val saverScope = SaverScope { true }

    @Test
    fun `saver restores hidden state`() {
        val state = VerticalSlidingDrawerState(DragAnchors.Hidden)
        val saved = VerticalSlidingDrawerStateSaver.run { saverScope.save(state) }
        val restored = VerticalSlidingDrawerStateSaver.restore(saved!!)

        assertEquals(DragAnchors.Hidden, restored?.currentValue)
    }

    @Test
    fun `saver restores expanded state`() {
        val state = VerticalSlidingDrawerState(DragAnchors.Expanded)
        val saved = VerticalSlidingDrawerStateSaver.run { saverScope.save(state) }
        val restored = VerticalSlidingDrawerStateSaver.restore(saved!!)

        assertEquals(DragAnchors.Expanded, restored?.currentValue)
    }
}
