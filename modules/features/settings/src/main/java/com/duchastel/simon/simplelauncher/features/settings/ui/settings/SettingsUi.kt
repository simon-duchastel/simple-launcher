package com.duchastel.simon.simplelauncher.features.settings.ui.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(state: SettingsState, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopStart,
    ) {
        state.settingsRows.forEach { settingsRow ->
            TextButton(
                onClick = { state.onSettingsRowClick(settingsRow) },
            ) {
                Text(text = settingsRow.label)
            }
        }
    }
}