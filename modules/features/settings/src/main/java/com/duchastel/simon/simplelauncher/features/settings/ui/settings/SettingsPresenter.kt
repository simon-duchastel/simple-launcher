package com.duchastel.simon.simplelauncher.features.settings.ui.settings

import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection.WidgetSelectionScreen
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingScreen
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList

class SettingsPresenter @AssistedInject internal constructor(
    @Assisted private val navigator: Navigator,
) : Presenter<SettingsState> {

    @Composable
    override fun present(): SettingsState {
        return SettingsState(
            settingsRows = SettingsState.SettingsRow.entries.toImmutableList(),
            onSettingsRowClick = { settingsRow ->
                when (settingsRow.toSettingDomainType()) {
                    Setting.WidgetConfiguration -> navigator.goTo(WidgetSelectionScreen)
                    else -> navigator.goTo(ModifySettingScreen(settingsRow.toSettingDomainType()))
                }
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPresenter
    }
}