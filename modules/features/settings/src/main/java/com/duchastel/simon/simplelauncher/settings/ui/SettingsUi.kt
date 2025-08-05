package com.duchastel.simon.simplelauncher.settings.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Settings(state: SettingsState, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        state.settingsRows.forEach { settingsRow ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { state.onSettingsRowClick(settingsRow) }
                    .padding(vertical = 16.dp)
            ) {
                Text(text = settingsRow.label)
            }
        }
    }
}
