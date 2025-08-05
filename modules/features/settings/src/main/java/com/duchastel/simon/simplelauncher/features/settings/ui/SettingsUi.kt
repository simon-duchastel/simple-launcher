package com.duchastel.simon.simplelauncher.features.settings.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.ui.Ui

class SettingsUi : Ui<SettingsState> {
    @Composable
    override fun Content(state: SettingsState, modifier: Modifier) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Settings Screen")
        }
    }
}