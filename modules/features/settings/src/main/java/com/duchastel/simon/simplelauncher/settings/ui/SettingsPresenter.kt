package com.duchastel.simon.simplelauncher.settings.ui

import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.settings.data.SettingsRepository
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object SettingsScreen: Screen

data class SettingsState(
    val settingsRows: ImmutableList<SettingsRow>,
    val onSettingsRowClick: (SettingsRow) -> Unit,
): CircuitUiState {
    enum class SettingsRow(val label: String) {
        HOMEPAGE_ACTION("Homepage Action"),
    }
}

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