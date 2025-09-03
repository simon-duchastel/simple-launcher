package com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError

@Composable
fun WidgetErrorView(
    error: WidgetError,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (error) {
                is WidgetError.ProviderNotFound -> "Widget provider not found"
                is WidgetError.PermissionDenied -> "Permission denied"
                is WidgetError.HostCreationFailed -> "Widget creation failed"
                is WidgetError.WidgetBindingFailed -> "Widget binding failed"
                is WidgetError.Unknown -> "Widget error: ${error.message}"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
    }
}