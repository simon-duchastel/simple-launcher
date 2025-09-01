package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ButtonState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.HomepageActionState

@Composable
fun ModifySettingContent(state: ModifySettingState, modifier: Modifier = Modifier) {
    when (state) {
        is HomepageActionState -> {
            HomepageActionContent(state, modifier)
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