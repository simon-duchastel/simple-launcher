package com.duchastel.simon.simplelauncher.features.settings.ui.settings

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingScreen
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsState.SettingsRow
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsPresenterTest {

    private val navigator: FakeNavigator = FakeNavigator(SettingsScreen)

    private val presenter = SettingsPresenter(navigator)

    @Test
    fun `presenter returns all settings rows`() = runTest {
        presenter.test {
            val state = awaitItem()

            val expected = listOf(
                SettingsRow.HOMEPAGE_ACTION,
            ).toImmutableList()
            Assert.assertEquals(expected, state.settingsRows)
        }
    }

    @Test
    fun `onSettingsRowClicked navigates to modify setting screen`() = runTest {
        presenter.test {
            val state = awaitItem()

            state.onSettingsRowClick(SettingsRow.HOMEPAGE_ACTION)
            val navigatedScreen = navigator.awaitNextScreen()
            Assert.assertEquals(ModifySettingScreen(Setting.HomepageAction), navigatedScreen)
        }
    }
}