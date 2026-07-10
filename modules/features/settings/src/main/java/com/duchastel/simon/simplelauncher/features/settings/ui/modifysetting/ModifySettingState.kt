package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetProviderInfo
import com.slack.circuit.runtime.CircuitUiState

sealed interface ModifySettingState : CircuitUiState

data class HomepageActionState(
    val saveButtonState: ButtonState,
    val onSaveButtonClicked: () -> Unit,
    val emoji: String,
    val isEmojiError: Boolean,
    val phoneNumber: String,
    val isPhoneNumberError: Boolean,
    val onEmojiChanged: (updatedEmoji: String) -> Unit,
    val onPhoneNumberChanged: (updatedPhoneNumber: String) -> Unit,
    val onChooseFromContactsClicked: () -> Unit,
) : ModifySettingState

data class CenterWidgetState(
    val availableWidgets: List<WidgetProviderInfo>,
    val currentWidget: WidgetData?,
    val isLoading: Boolean,
    val errorMessage: String?,
    val onWidgetSelected: (WidgetProviderInfo) -> Unit,
    val onClearWidget: () -> Unit,
) : ModifySettingState

sealed interface ButtonState {
    data object Loading: ButtonState
    data object Enabled: ButtonState
    data object Disabled: ButtonState
}
