package com.duchastel.simon.simplelauncher.features.settings.ui.settings

import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.slack.circuit.runtime.CircuitUiState
import kotlinx.collections.immutable.ImmutableList

data class SettingsState(
    val settingsRows: ImmutableList<SettingsRow>,
    val onSettingsRowClick: (SettingsRow) -> Unit,
): CircuitUiState {
    enum class SettingsRow(val label: String) {
        HOMEPAGE_ACTION("Homepage Action"),
        WIDGET_CONFIGURATION("Homepage Widget"),
    }
}

internal fun SettingsState.SettingsRow.toSettingDomainType(): Setting {
    return when (this) {
        SettingsState.SettingsRow.HOMEPAGE_ACTION -> Setting.HomepageAction
        SettingsState.SettingsRow.WIDGET_CONFIGURATION -> Setting.WidgetConfiguration
    }
}