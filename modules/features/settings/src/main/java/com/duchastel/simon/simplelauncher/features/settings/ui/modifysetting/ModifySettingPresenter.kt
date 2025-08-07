package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsPresenter
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data class ModifySettingScreen(val setting: Setting) : Screen

data class ModifySettingState(
    val setting: Setting,
): CircuitUiState

class ModifySettingPresenter @AssistedInject internal constructor(
    @Assisted private val screen: ModifySettingScreen,
    @Assisted private val navigator: Navigator,
    private val settingsRepository: SettingsRepository,
) : Presenter<ModifySettingState> {

    @Composable
    override fun present(): ModifySettingState {
        return ModifySettingState(screen.setting)
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: ModifySettingScreen, navigator: Navigator): ModifySettingPresenter
    }
}

