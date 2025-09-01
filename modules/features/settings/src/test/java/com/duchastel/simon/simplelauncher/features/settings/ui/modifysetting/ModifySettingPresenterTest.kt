package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsScreen
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ModifySettingPresenterTest {

    private val repository: SettingsRepository = mock()
    private val permissionsRepository: PermissionsRepository = mock()
    private val contactsRepository: ContactsRepository = mock()
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
            permissionsRepository,
            contactsRepository,
        )
    }

    @Test
    fun `presenter returns HomepageActionState when HomepageAction is passed`() = runTest {
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem()

            assert(state is ModifySettingState.HomepageActionState)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - present sets initial state from repository valid data`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            assert(state.emoji == "😊")
            assert(!state.isEmojiError)
            assert(state.phoneNumber == "1234567890")
            assert(!state.isPhoneNumberError)
            assert(state.saveButtonState == ModifySettingState.ButtonState.Enabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - emoji change with invalid input sets error and does not update value`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("A")
            runCurrent()
            assert(state.isEmojiError)
            assert(state.emoji == "😊")
            assert(state.saveButtonState == ModifySettingState.ButtonState.Disabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - emoji change with valid emoji updates value and clears error`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("🙀")
            runCurrent()
            assert(!state.isEmojiError)
            assert(state.emoji == "🙀")
            assert(state.saveButtonState == ModifySettingState.ButtonState.Enabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - phone number change with invalid input sets error and disables save`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "😂", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onPhoneNumberChanged("NotAPhone")
            runCurrent()
            assert(state.isPhoneNumberError)
            assert(state.saveButtonState == ModifySettingState.ButtonState.Disabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - phone number change with valid input updates value and enables save`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "😂", phoneNumber = "")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onPhoneNumberChanged("5145550123")
            runCurrent()
            assert(!state.isPhoneNumberError)
            assert(state.phoneNumber == "5145550123")
            assert(state.saveButtonState == ModifySettingState.ButtonState.Enabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - save button is disabled when emoji error present`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "5145550123")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("x")
            runCurrent()
            assert(state.isEmojiError)
            assert(state.saveButtonState == ModifySettingState.ButtonState.Disabled)
        }
    }

    @Ignore("TODO - fix once regex pattern is mocked")
    @Test
    fun `HomepageAction - save calls repository and pops navigator on success`() = runTest {
        val initial = SettingData.HomepageActionSettingData(emoji = "🍕", phoneNumber = "5145550123")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial))
        whenever(repository.saveSetting(any<SettingData.HomepageActionSettingData>())).thenReturn(
            true
        )
        setupPresenter(Setting.HomepageAction)
        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onSaveButtonClicked()
            runCurrent()
            verify(repository).saveSetting(
                SettingData.HomepageActionSettingData("🍕", "5145550123")
            )

            navigator.awaitPop()
        }
    }
}