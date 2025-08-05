package com.duchastel.simon.simplelauncher.features.settings.ui

import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize

data class SettingsState(
    val settingsRows: ImmutableList<SettingsRow>,
    val onSettingsRowClick: (SettingsRow) -> Unit,
): CircuitUiState {
    enum class SettingsRow(val label: String) {
        HOMEPAGE_ACTION("Homepage Action"),
    }
}