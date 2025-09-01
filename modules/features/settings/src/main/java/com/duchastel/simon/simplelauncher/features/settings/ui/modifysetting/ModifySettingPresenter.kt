package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ButtonState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.HomepageActionState
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
    private val permissionsRepository: PermissionsRepository,
    private val contactsRepository: ContactsRepository,
) : Presenter<ModifySettingState> {

    @Composable
    override fun present(): ModifySettingState {
        return when (screen.setting) {
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
                            isEmojiError = !it.emoji.isEmoji()
                            phoneNumber = it.phoneNumber
                            isPhoneNumberError = !phoneNumber.isValidPhoneNumber()
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
                            val hasError = (updatedEmoji.isNotEmpty() && !updatedEmoji.isEmoji())
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
                            isPhoneNumberError = phoneNumber.isNotEmpty() && !updatedPhoneNumber.isValidPhoneNumber()
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
                                    isPhoneNumberError = phoneNumber.isNotEmpty() && !phoneNumber.isValidPhoneNumber()
                                }
                            }
                        }
                    },
                )
            }
        }
    }

    private fun String.isEmoji(): Boolean {
        val asChar = firstOrNull() ?: return false
        return Character.getType(asChar) == Character.SURROGATE.toInt() ||
                Character.getType(asChar) == Character.OTHER_SYMBOL.toInt()
    }

    private fun String.isValidPhoneNumber(): Boolean {
        return Patterns.PHONE.matcher(this).matches()
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: ModifySettingScreen, navigator: Navigator): ModifySettingPresenter
    }
}

