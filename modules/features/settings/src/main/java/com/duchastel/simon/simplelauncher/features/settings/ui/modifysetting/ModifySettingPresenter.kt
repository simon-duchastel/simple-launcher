package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.libs.phonenumber.data.PhoneNumberValidator
import com.duchastel.simon.simplelauncher.libs.emoji.data.EmojiValidator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection.WidgetSelectionResult
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection.WidgetSelectionScreen
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ButtonState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.HomepageActionState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.WidgetConfigurationState
import com.duchastel.simon.simplelauncher.libs.permissions.data.Permission
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModifySettingScreen(val setting: Setting) : Screen

class ModifySettingPresenter @AssistedInject internal constructor(
    @Assisted private val screen: ModifySettingScreen,
    @Assisted private val navigator: Navigator,
    private val settingsRepository: SettingsRepository,
    private val appWidgetRepository: AppWidgetRepository,
    private val permissionsRepository: PermissionsRepository,
    private val contactsRepository: ContactsRepository,
    private val phoneNumberValidator: PhoneNumberValidator,
    private val emojiValidator: EmojiValidator,
) : Presenter<ModifySettingState> {

    @Composable
    override fun present(): ModifySettingState {
        return when (screen.setting) {
            Setting.WidgetConfiguration -> {
                var currentWidget by remember { mutableStateOf<WidgetData?>(null) }
                var saveButtonState: ButtonState by remember { mutableStateOf(ButtonState.Loading) }
                val coroutineScope = rememberCoroutineScope()
                
                // Load current widget setting
                LaunchedEffect(Unit) {
                    settingsRepository.getSettingsFlow(Setting.WidgetConfiguration)?.collect { settingData ->
                        currentWidget = (settingData as? SettingData.WidgetConfigurationSettingData)?.widgetData
                        saveButtonState = ButtonState.Disabled // No save needed for widget config
                    }
                }
                
                // TODO: Handle navigation result from widget selection
                // For now, this is commented out until we fix the Circuit navigation result handling
                
                WidgetConfigurationState(
                    saveButtonState = saveButtonState,
                    onSaveButtonClicked = { /* No-op, save happens automatically */ },
                    currentWidget = currentWidget,
                    onSelectWidgetClicked = {
                        navigator.goTo(WidgetSelectionScreen(currentWidget))
                    },
                    onClearWidgetClicked = {
                        coroutineScope.launch {
                            // Remove current widget if exists
                            currentWidget?.let { widget ->
                                appWidgetRepository.unbindWidget(widget.widgetId)
                            }
                            
                            // Clear widget setting
                            val settingData = SettingData.WidgetConfigurationSettingData(null)
                            val saveSuccess = settingsRepository.saveSetting(settingData)
                            if (saveSuccess) {
                                currentWidget = null
                                // Navigate back to settings
                                navigator.pop()
                            }
                        }
                    }
                )
            }
            
            Setting.HomepageAction -> {
                var emoji by remember { mutableStateOf("") }
                var isEmojiError by remember { mutableStateOf(false) }
                var phoneNumber by remember { mutableStateOf("") }
                var isPhoneNumberError by remember { mutableStateOf(false) }
                var saveButtonState: ButtonState by remember { mutableStateOf(ButtonState.Loading) }

                LaunchedEffect(Unit) {
                    settingsRepository.getSettingsFlow(Setting.HomepageAction)?.collect {
                        saveButtonState = ButtonState.Enabled
                        (it as? SettingData.HomepageActionSettingData)?.let {
                            emoji = it.emoji
                            isEmojiError = !emojiValidator.isEmoji(it.emoji)
                            phoneNumber = it.phoneNumber
                            isPhoneNumberError = !phoneNumberValidator.isValidPhoneNumber(phoneNumber)
                        }
                    }
                }
                LaunchedEffect(isPhoneNumberError, isEmojiError) {
                    // don't update the save button if we're loading
                    if (saveButtonState is ButtonState.Loading) return@LaunchedEffect

                    saveButtonState = if (isEmojiError || isPhoneNumberError) {
                        ButtonState.Disabled
                    } else {
                        ButtonState.Enabled
                    }
                }

                val coroutineScope = rememberCoroutineScope()
                HomepageActionState(
                    emoji = emoji,
                    isEmojiError = isEmojiError,
                    phoneNumber = phoneNumber,
                    isPhoneNumberError = isPhoneNumberError,
                    onEmojiChanged = { updatedEmoji ->
                        // don't process the change if we're loading
                        if (saveButtonState !is ButtonState.Loading) {
                            val hasError = (updatedEmoji.isNotEmpty() && !emojiValidator.isEmoji(updatedEmoji))
                            if (!hasError) {
                                // only update the emoji if it's valid
                                emoji = updatedEmoji
                            }
                            isEmojiError = hasError
                        }
                    },
                    onPhoneNumberChanged = { updatedPhoneNumber ->
                        // don't process the change if we're loading
                        if (saveButtonState !is ButtonState.Loading) {
                            phoneNumber = updatedPhoneNumber
                            isPhoneNumberError = phoneNumber.isNotEmpty() && !phoneNumberValidator.isValidPhoneNumber(updatedPhoneNumber)
                        }
                    },
                    saveButtonState = saveButtonState,
                    onSaveButtonClicked = {
                        coroutineScope.launch {
                            val saveSuccessful = settingsRepository.saveSetting(
                                SettingData.HomepageActionSettingData(
                                    emoji = emoji,
                                    phoneNumber = phoneNumber
                                )
                            )
                            if (saveSuccessful) {
                                navigator.pop()
                            }
                        }
                    },
                    onChooseFromContactsClicked = {
                        coroutineScope.launch {
                            val permissionGranted = permissionsRepository.requestPermission(Permission.READ_CONTACTS)
                            if (permissionGranted) {
                                val contact = contactsRepository.pickContact()
                                contact?.let {
                                    phoneNumber = it.phoneNumber
                                    isPhoneNumberError = phoneNumber.isNotEmpty() && !phoneNumberValidator.isValidPhoneNumber(phoneNumber)
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: ModifySettingScreen, navigator: Navigator): ModifySettingPresenter
    }
}

