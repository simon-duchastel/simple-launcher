package com.duchastel.simon.simplelauncher.features.settings.ui.settings

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

data class SettingsState(
    val settingsRows: ImmutableList<SettingsRow>,
    val onSettingsRowClick: (SettingsRow) -> Unit,
): CircuitUiState {
    enum class SettingsRow(val label: String) {
        HOMEPAGE_ACTION("Homepage Action"),
    }
}