package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsScreen
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class ModifySettingPresenterTest {

    private val repository: SettingsRepository = mock()
    private val navigator: FakeNavigator = FakeNavigator(SettingsScreen)

    private lateinit var presenter: ModifySettingPresenter

    @Before
    fun setup() {
        setupPresenter(Setting.HomepageAction)
    }

    fun setupPresenter(setting: Setting) {
        presenter = ModifySettingPresenter(
            ModifySettingScreen(setting),
            navigator,
            repository,
        )
    }

    @Test
    fun `presenter returns setting from config`() = runTest {
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem()

            assertEquals(Setting.HomepageAction, state.setting)
        }
    }
}