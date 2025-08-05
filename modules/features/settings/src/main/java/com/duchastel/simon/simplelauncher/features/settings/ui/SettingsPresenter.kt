package com.duchastel.simon.simplelauncher.features.settings.ui

import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.toImmutableList

class SettingsPresenter @AssistedInject internal constructor(
    @Assisted private val navigator: Navigator,
    private val repository: SettingsRepository,
) : Presenter<SettingsState> {

    @Composable
    override fun present(): SettingsState {
        return SettingsState(
            settingsRows = SettingsState.SettingsRow.entries.toImmutableList(),
            onSettingsRowClick = { settingsRow ->
                // TODO - add navigation to settings row screen
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator): SettingsPresenter
    }
}