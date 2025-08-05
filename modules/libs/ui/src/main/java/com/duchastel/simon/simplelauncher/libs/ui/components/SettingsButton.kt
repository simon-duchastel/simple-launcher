package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Filled.Settings,
            contentDescription = "Settings"
        )
    }
}

@Preview
@Composable
fun SettingsButtonPreview() {
    SettingsButton(onClick = {})
}
