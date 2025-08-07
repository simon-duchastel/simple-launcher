package com.duchastel.simon.simplelauncher.features.homepage.ui

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.slack.circuit.test.test
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class HomepagePresenterTest {

    private val settingsRepository: SettingsRepository = mock()
    private val presenter = HomepagePresenter(settingsRepository)

    @Test
    fun `presenter provides default state when no homepage action setting exists`() = runTest {
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(null))

        presenter.test {
            val state = awaitItem()
            assert(state.text == "Welcome back...")
            assert(state.homepageAction == null)
        }
    }

    @Test
    fun `presenter provides state with homepage action when setting exists`() = runTest {
        val homepageActionSetting = SettingData.HomepageActionSettingData(
            emoji = "👋",
            phoneNumber = "1234567890"
        )
        whenever(settingsRepository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(homepageActionSetting))

        presenter.test {
            val state = expectMostRecentItem()

            Assert.assertEquals("Welcome back...", state.text)
            Assert.assertEquals("👋", state.homepageAction?.emoji)
            Assert.assertEquals("1234567890", state.homepageAction?.smsDestination)
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
}
