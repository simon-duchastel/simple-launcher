package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetProviderInfo
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ButtonState

@Composable
fun ModifySettingContent(state: ModifySettingState, modifier: Modifier = Modifier) {
    when (state) {
        is HomepageActionState -> {
            HomepageActionContent(state, modifier)
        }
        is CenterWidgetState -> {
            CenterWidgetContent(state, modifier)
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
private fun CenterWidgetContent(state: CenterWidgetState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        state.currentWidget?.label?.let { label ->
            Text(
                text = "Current widget: $label",
                style = MaterialTheme.typography.titleMedium,
            )
        }

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Button(
            onClick = { state.onClearWidget() },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
        ) {
            Text("Clear widget")
        }

        Text(
            text = "Select a widget",
            style = MaterialTheme.typography.titleMedium,
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(
                    items = state.availableWidgets,
                    key = { it.componentName },
                ) { widget ->
                    WidgetListItem(
                        widget = widget,
                        onClick = { state.onWidgetSelected(widget) },
                        enabled = !state.isLoading,
                    )
                }
            }
        }
    }
}

@Composable
private fun WidgetListItem(
    widget: WidgetProviderInfo,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Text(
        text = widget.label,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick)
            .padding(16.dp),
        style = MaterialTheme.typography.bodyLarge,
    )
}
