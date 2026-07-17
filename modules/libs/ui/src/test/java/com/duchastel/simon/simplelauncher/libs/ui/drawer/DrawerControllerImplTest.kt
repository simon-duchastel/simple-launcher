package com.duchastel.simon.simplelauncher.libs.ui.drawer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DrawerControllerImplTest {

    @Test
    fun `requestClose emits a close request`() = runTest {
        val controller = DrawerControllerImpl()
        var received = false
        val job = launch(UnconfinedTestDispatcher()) {
            controller.closeRequests.collect { received = true }
        }

        controller.requestClose()

        assertTrue(received)
        job.cancel()
    }
}
