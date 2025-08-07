package com.duchastel.simon.simplelauncher.features.applist.ui

import android.content.Context
import com.duchastel.simon.simplelauncher.features.applist.data.App
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AppListPresenterTest {
    private val context: Context = mock()
    private val appRepository: AppRepository = mock()
    private val intentLauncher: IntentLauncher = mock()
    private val presenter = AppListPresenter(context, appRepository, intentLauncher)

    @Test
    fun `state contains apps from repository`() = runTest {
        val fakeApps = listOf(
            App(label = "App1", packageName = "com.example.app1", icon = mock()),
            App(label = "App2", packageName = "com.example.app2", icon = mock())
        )
        whenever(appRepository.getInstalledApps()).thenReturn(fakeApps)

        presenter.test {
            val state = awaitItem()

            assertEquals(2, state.apps.size)
            assertEquals("App1", state.apps[0].label)
            assertEquals("App2", state.apps[1].label)
        }
    }

    @Test
    fun `onLaunchApp calls repository`() = runTest {
        val fakeApp = App(label = "App1", packageName = "com.example.app1", icon = mock())
        whenever(appRepository.getInstalledApps()).thenReturn(listOf(fakeApp))

        presenter.test {
            val state = awaitItem()
            state.apps[0].launchApp()
            verify(appRepository).launchApp(fakeApp)
        }
    }

    fun `onSettingsClicked starts settings activity as new app`() = runTest {
        presenter.test {
            val state = awaitItem()
            state.onSettingsClicked()
            verify(intentLauncher).startActivityAsSeparateApp(any())
        }
    }
}
