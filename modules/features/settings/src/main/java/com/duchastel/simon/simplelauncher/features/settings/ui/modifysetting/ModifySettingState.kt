package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.slack.circuit.runtime.CircuitUiState

sealed interface ModifySettingState : CircuitUiState {
    val saveButtonState: ButtonState
    val onSaveButtonClicked: () -> Unit

    data class HomepageActionState(
        override val saveButtonState: ButtonState,
        override val onSaveButtonClicked: () -> Unit,
        val emoji: String,
        val isEmojiError: Boolean,
        val phoneNumber: String,
        val isPhoneNumberError: Boolean,
        val onEmojiChanged: (updatedEmoji: String) -> Unit,
        val onPhoneNumberChanged: (updatedPhoneNumber: String) -> Unit,
        val onChooseFromContactsClicked: () -> Unit,
    ) : ModifySettingState

    data class WidgetConfigurationState(
        override val saveButtonState: ButtonState,
        override val onSaveButtonClicked: () -> Unit,
        val currentWidget: WidgetData?,
        val onSelectWidgetClicked: () -> Unit,
        val onClearWidgetClicked: () -> Unit,
    ) : ModifySettingState

    sealed interface ButtonState {
        data object Loading: ButtonState
        data object Enabled: ButtonState
        data object Disabled: ButtonState
    }

}