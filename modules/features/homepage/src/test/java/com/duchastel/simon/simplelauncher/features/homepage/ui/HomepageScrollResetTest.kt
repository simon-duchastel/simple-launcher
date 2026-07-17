package com.duchastel.simon.simplelauncher.features.homepage.ui

import com.duchastel.simon.simplelauncher.libs.ui.components.DragAnchors
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class HomepageScrollResetTest {

    @Test
    fun `scroll reset is requested when drawer is hidden`() = runTest {
        val currentValueFlow = MutableStateFlow(DragAnchors.Expanded)
        val resets = mutableListOf<Unit>()

        val job = launch {
            resetAppListScrollOnDrawerHide(currentValueFlow) { resets.add(Unit) }
        }
        advanceUntilIdle()

        currentValueFlow.value = DragAnchors.Hidden
        advanceUntilIdle()

        job.cancel()

        assertEquals(1, resets.size)
    }

    @Test
    fun `scroll reset is not requested while drawer stays expanded`() = runTest {
        val currentValueFlow = MutableStateFlow(DragAnchors.Expanded)
        val resets = mutableListOf<Unit>()

        val job = launch {
            resetAppListScrollOnDrawerHide(currentValueFlow) { resets.add(Unit) }
        }
        advanceUntilIdle()

        currentValueFlow.value = DragAnchors.Expanded
        advanceUntilIdle()
        currentValueFlow.value = DragAnchors.Expanded
        advanceUntilIdle()

        job.cancel()

        assertEquals(0, resets.size)
    }

    @Test
    fun `scroll reset is requested each time drawer is hidden`() = runTest {
        val currentValueFlow = MutableStateFlow(DragAnchors.Expanded)
        val resets = mutableListOf<Unit>()

        val job = launch {
            resetAppListScrollOnDrawerHide(currentValueFlow) { resets.add(Unit) }
        }
        advanceUntilIdle()

        currentValueFlow.value = DragAnchors.Hidden
        advanceUntilIdle()
        currentValueFlow.value = DragAnchors.Expanded
        advanceUntilIdle()
        currentValueFlow.value = DragAnchors.Hidden
        advanceUntilIdle()

        job.cancel()

        assertEquals(2, resets.size)
    }
}
