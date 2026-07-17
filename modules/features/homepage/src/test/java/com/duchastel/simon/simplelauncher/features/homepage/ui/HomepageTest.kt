package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.content.Context
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.libs.ui.drawer.DrawerController
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.slack.circuit.test.test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomepagePresenterTest {

    private val context: Context = mock()
    private val settingsRepository: SettingsRepository = mock()
    private val intentLauncher: IntentLauncher = mock()
    private val drawerController: DrawerController = mock()
    private val presenter = HomepagePresenter(context, settingsRepository, intentLauncher, drawerController)

    init {
        whenever(drawerController.closeRequests).thenReturn(flowOf(Unit))
    }

    @Test
    fun `presenter provides default state when no homepage action setting exists`() = runTest {
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(null))
        whenever(settingsRepository.getSettingsFlow(Setting.CenterWidget)).thenReturn(flowOf(null))

        presenter.test {
            val state = awaitItem()
            assert(state.homepageAction == null)
            assert(state.centerWidget == null)
        }
    }

    @Test
    fun `presenter provides state with homepage action when setting exists`() = runTest {
        val homepageActionSetting = SettingData.HomepageActionSettingData(
            emoji = "👋",
            phoneNumber = "1234567890"
        )
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(homepageActionSetting))
        whenever(settingsRepository.getSettingsFlow(Setting.CenterWidget)).thenReturn(flowOf(null))

        presenter.test {
            val state = expectMostRecentItem()

            Assert.assertEquals("👋", state.homepageAction?.emoji)
            Assert.assertEquals("1234567890", state.homepageAction?.smsDestination)
            Assert.assertEquals(null, state.centerWidget)
        }
    }

    @Test
    fun `presenter provides state with center widget when setting exists`() = runTest {
        val widgetData = WidgetData(
            widgetId = 1,
            providerComponentName = "com.example/ClockWidget",
            width = 200,
            height = 100,
        )
        val centerWidgetSetting = SettingData.CenterWidgetSettingData(widgetData)
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(null))
        whenever(settingsRepository.getSettingsFlow(Setting.CenterWidget)).thenReturn(flowOf(centerWidgetSetting))

        presenter.test {
            val state = expectMostRecentItem()

            Assert.assertEquals(null, state.homepageAction)
            Assert.assertEquals(widgetData, state.centerWidget)
        }
    }

    @Test
    fun `presenter updates state when homepage action setting changes`() = runTest {
        val initialSetting = SettingData.HomepageActionSettingData(
            emoji = "👋",
            phoneNumber = "1234567890"
        )
        val settingsFlow = MutableStateFlow<SettingData?>(initialSetting)
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(settingsFlow)
        whenever(settingsRepository.getSettingsFlow(Setting.CenterWidget)).thenReturn(flowOf(null))

        presenter.test {
            val initialState = expectMostRecentItem()
            Assert.assertEquals("👋", initialState.homepageAction?.emoji)
            Assert.assertEquals("1234567890", initialState.homepageAction?.smsDestination)

            settingsFlow.value = SettingData.HomepageActionSettingData(
                emoji = "🚀",
                phoneNumber = "0987654321"
            )

            val updatedState = awaitItem()
            Assert.assertEquals("🚀", updatedState.homepageAction?.emoji)
            Assert.assertEquals("0987654321", updatedState.homepageAction?.smsDestination)
        }
    }

    @Test
    fun `presenter updates state when center widget setting changes`() = runTest {
        val initialWidgetData = WidgetData(
            widgetId = 1,
            providerComponentName = "com.example/ClockWidget",
            width = 200,
            height = 100,
        )
        val initialSetting = SettingData.CenterWidgetSettingData(initialWidgetData)
        val settingsFlow = MutableStateFlow<SettingData?>(initialSetting)
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(null))
        whenever(settingsRepository.getSettingsFlow(Setting.CenterWidget)).thenReturn(settingsFlow)

        presenter.test {
            val initialState = expectMostRecentItem()
            Assert.assertEquals(initialWidgetData, initialState.centerWidget)

            val updatedWidgetData = WidgetData(
                widgetId = 2,
                providerComponentName = "com.example/WeatherWidget",
                width = 300,
                height = 150,
            )
            settingsFlow.value = SettingData.CenterWidgetSettingData(updatedWidgetData)

            val updatedState = awaitItem()
            Assert.assertEquals(updatedWidgetData, updatedState.centerWidget)
        }
    }
}
