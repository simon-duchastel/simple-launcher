package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

import com.slack.circuit.runtime.ui.ui

class ModifySettingUiFactory : Ui.Factory {
    override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
        return if (screen is ModifySettingScreen) {
            ui<ModifySettingState> { state, modifier ->
                ModifySettingContent(state, modifier)
            }
        } else {
            null
        }
    }
}