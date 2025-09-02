package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsScreen
import com.duchastel.simon.simplelauncher.libs.permissions.data.Permission
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.contacts.data.Contact
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.duchastel.simon.simplelauncher.libs.phonenumber.data.PhoneNumberValidator
import com.duchastel.simon.simplelauncher.libs.emoji.data.EmojiValidator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
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
    private val phoneNumberValidator: PhoneNumberValidator = mock()
    private val emojiValidator: EmojiValidator = mock()
    private val navigator: FakeNavigator = FakeNavigator(SettingsScreen)

    private lateinit var presenter: ModifySettingPresenter

    @Before
    fun setup() {
        setupPresenter(Setting.HomepageAction)
    }

    fun setupPresenter(setting: Setting) {
        whenever(phoneNumberValidator.isValidPhoneNumber(any())).thenReturn(true)
        whenever(emojiValidator.isEmoji(any())).thenReturn(true)

        presenter = ModifySettingPresenter(
            ModifySettingScreen(setting),
            navigator,
            repository,
            permissionsRepository,
            contactsRepository,
            phoneNumberValidator,
            emojiValidator,
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

    @Test
    fun `HomepageAction - present sets initial state from repository valid data`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(phoneNumberValidator.isValidPhoneNumber("1234567890")).thenReturn(true)
        whenever(emojiValidator.isEmoji("😊")).thenReturn(true)

        presenter.test {
            awaitItem() // Skip initial loading state
            val state = awaitItem() as ModifySettingState.HomepageActionState

            assert(state.emoji == "😊")
            assert(!state.isEmojiError)
            assert(state.phoneNumber == "1234567890")
            assert(!state.isPhoneNumberError)
            assert(state.saveButtonState == ModifySettingState.ButtonState.Enabled)
        }
    }

    @Test
    fun `HomepageAction - emoji change with invalid input sets error and does not update value`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(phoneNumberValidator.isValidPhoneNumber("1234567890")).thenReturn(true)
        whenever(emojiValidator.isEmoji("1")).thenReturn(false)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("1")
            runCurrent()

            val stateWithEmojiChanged = expectMostRecentItem() as ModifySettingState.HomepageActionState
            assert(stateWithEmojiChanged.isEmojiError)
            assert(stateWithEmojiChanged.emoji == "😊")
            assert(stateWithEmojiChanged.saveButtonState == ModifySettingState.ButtonState.Disabled)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - emoji change with valid emoji updates value and clears error`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(phoneNumberValidator.isValidPhoneNumber("1234567890")).thenReturn(true)
        whenever(emojiValidator.isEmoji("🙀")).thenReturn(true)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("🙀")
            runCurrent()

            val stateWithEmojiChanged = expectMostRecentItem() as ModifySettingState.HomepageActionState
            assert(!stateWithEmojiChanged.isEmojiError)
            assert(stateWithEmojiChanged.emoji == "🙀")
            assert(stateWithEmojiChanged.saveButtonState == ModifySettingState.ButtonState.Enabled)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - phone number change with invalid input sets error and disables save`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😂", phoneNumber = "1234567890")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(phoneNumberValidator.isValidPhoneNumber("1234567890")).thenReturn(true)
        whenever(phoneNumberValidator.isValidPhoneNumber("NotAPhone")).thenReturn(false)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onPhoneNumberChanged("NotAPhone")
            runCurrent()

            val stateWithChangedPhoneNumber = expectMostRecentItem() as ModifySettingState.HomepageActionState
            assert(stateWithChangedPhoneNumber.isPhoneNumberError)
            assert(stateWithChangedPhoneNumber.saveButtonState == ModifySettingState.ButtonState.Disabled)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - phone number change with valid input updates value and enables save`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😂", phoneNumber = "")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(phoneNumberValidator.isValidPhoneNumber("")).thenReturn(false)
        whenever(phoneNumberValidator.isValidPhoneNumber("5145550123")).thenReturn(true)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onPhoneNumberChanged("5145550123")
            runCurrent()

            val stateWithChangedPhoneNumber = expectMostRecentItem() as ModifySettingState.HomepageActionState
            assert(!stateWithChangedPhoneNumber.isPhoneNumberError)
            assert(stateWithChangedPhoneNumber.phoneNumber == "5145550123")
            assert(stateWithChangedPhoneNumber.saveButtonState == ModifySettingState.ButtonState.Enabled)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    
    @Test
    fun `HomepageAction - save button is disabled when emoji error present`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "5145550123")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(emojiValidator.isEmoji("X")).thenReturn(false)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onEmojiChanged("X")
            runCurrent()

            val stateWithError = expectMostRecentItem() as ModifySettingState.HomepageActionState
            assert(stateWithError.isEmojiError)
            assert(stateWithError.saveButtonState == ModifySettingState.ButtonState.Disabled)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - save calls repository and pops navigator on success`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "🍕", phoneNumber = "5145550123")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(repository.saveSetting(any<SettingData.HomepageActionSettingData>())).thenReturn(
            true
        )
        whenever(phoneNumberValidator.isValidPhoneNumber("5145550123")).thenReturn(true)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onSaveButtonClicked()
            runCurrent()
            verify(repository).saveSetting(
                SettingData.HomepageActionSettingData("🍕", "5145550123")
            )

            navigator.awaitPop()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - onChooseFromContactsClicked when permission is denied`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(permissionsRepository.requestPermission(Permission.READ_CONTACTS)).thenReturn(false)
        whenever(phoneNumberValidator.isValidPhoneNumber("")).thenReturn(false)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onChooseFromContactsClicked()
            runCurrent()
            
            verify(permissionsRepository).requestPermission(Permission.READ_CONTACTS)
            assert(state.phoneNumber.isEmpty())

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - onChooseFromContactsClicked when permission granted and contact selected`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "")
        val contact = Contact(id = "123", phoneNumber = "5145550123")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(permissionsRepository.requestPermission(Permission.READ_CONTACTS)).thenReturn(true)
        whenever(contactsRepository.pickContact()).thenReturn(contact)
        whenever(phoneNumberValidator.isValidPhoneNumber("")).thenReturn(false)
        whenever(phoneNumberValidator.isValidPhoneNumber("5145550123")).thenReturn(true)

        presenter.test {
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onChooseFromContactsClicked()
            runCurrent()

            val stateWithContact = expectMostRecentItem() as ModifySettingState.HomepageActionState
            verify(permissionsRepository).requestPermission(Permission.READ_CONTACTS)
            verify(contactsRepository).pickContact()
            assert(stateWithContact.phoneNumber == "5145550123")

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `HomepageAction - onChooseFromContactsClicked when permission granted but no contact selected`() = runTest {
        setupPresenter(Setting.HomepageAction)

        val initial = SettingData.HomepageActionSettingData(emoji = "😊", phoneNumber = "initialNumber")
        whenever(repository.getSettingsFlow(Setting.HomepageAction)).thenReturn(flowOf(initial as SettingData?))
        whenever(permissionsRepository.requestPermission(Permission.READ_CONTACTS)).thenReturn(true)
        whenever(contactsRepository.pickContact()).thenReturn(null)
        whenever(phoneNumberValidator.isValidPhoneNumber("initialNumber")).thenReturn(true)

        presenter.test {
            awaitItem() // Skip initial loading state
            val state = awaitItem() as ModifySettingState.HomepageActionState
            state.onChooseFromContactsClicked()
            advanceUntilIdle()
            
            verify(permissionsRepository).requestPermission(Permission.READ_CONTACTS)
            verify(contactsRepository).pickContact()
            assert(state.phoneNumber == "initialNumber")
        }
    }
}