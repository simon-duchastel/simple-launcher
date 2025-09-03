package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ButtonState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.HomepageActionState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.WidgetConfigurationState

@Composable
fun ModifySettingContent(state: ModifySettingState, modifier: Modifier = Modifier) {
    when (state) {
        is HomepageActionState -> {
            HomepageActionContent(state, modifier)
        }
        is WidgetConfigurationState -> {
            WidgetConfigurationContent(state, modifier)
        }
    }
}

@Composable
private fun HomepageActionContent(state: HomepageActionState, modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = state.emoji,
            onValueChange = { state.onEmojiChanged(it) },
            label = { Text("Emoji") },
            isError = state.isEmojiError,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = state.phoneNumber,
                onValueChange = { state.onPhoneNumberChanged(it) },
                label = { Text("Phone number") },
                isError = state.isPhoneNumberError,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { state.onChooseFromContactsClicked() }) {
                Text("Choose from contacts")
            }
        }
        TextButton(
            onClick = { state.onSaveButtonClicked() },
            enabled = state.saveButtonState is ButtonState.Enabled
        ) {
            if (state.saveButtonState is ButtonState.Loading) {
                CircularProgressIndicator()
            } else {
                Text("Save")
            }
        }
    }
}

@Composable
private fun WidgetConfigurationContent(state: WidgetConfigurationState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Widget Configuration",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Current widget display
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Current Widget",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                if (state.currentWidget != null) {
                    Text(
                        text = state.currentWidget.label ?: "Unknown Widget",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${state.currentWidget.width} × ${state.currentWidget.height}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Text(
                        text = "No widget selected",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Widget selection buttons
        Button(
            onClick = state.onSelectWidgetClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Select Widget")
        }
        
        if (state.currentWidget != null) {
            OutlinedButton(
                onClick = state.onClearWidgetClicked,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Clear Widget")
            }
        }
    }
}