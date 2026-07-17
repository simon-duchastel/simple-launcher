package com.duchastel.simon.simplelauncher.libs.ui.components

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class VerticalSlidingDrawerMarginTest {

    @Test
    fun `pointer at left edge is in margin when sheet is centered on wide screen`() {
        val maxWidthPx = 1200f
        val sheetMaxWidthPx = 800f

        assertTrue(isPointerInMargin(pointerX = 10f, maxWidthPx, sheetMaxWidthPx))
    }

    @Test
    fun `pointer at right edge is in margin when sheet is centered on wide screen`() {
        val maxWidthPx = 1200f
        val sheetMaxWidthPx = 800f

        assertTrue(isPointerInMargin(pointerX = 1190f, maxWidthPx, sheetMaxWidthPx))
    }

    @Test
    fun `pointer over centered sheet is not in margin`() {
        val maxWidthPx = 1200f
        val sheetMaxWidthPx = 800f

        assertFalse(isPointerInMargin(pointerX = 600f, maxWidthPx, sheetMaxWidthPx))
    }

    @Test
    fun `pointer at sheet edge is not in margin`() {
        val maxWidthPx = 1200f
        val sheetMaxWidthPx = 800f

        assertFalse(isPointerInMargin(pointerX = 200f, maxWidthPx, sheetMaxWidthPx))
        assertFalse(isPointerInMargin(pointerX = 1000f, maxWidthPx, sheetMaxWidthPx))
    }

    @Test
    fun `margin detection returns false when sheet fills the screen`() {
        val maxWidthPx = 800f
        val sheetMaxWidthPx = 800f

        assertFalse(isPointerInMargin(pointerX = 10f, maxWidthPx, sheetMaxWidthPx))
        assertFalse(isPointerInMargin(pointerX = 790f, maxWidthPx, sheetMaxWidthPx))
    }
}
