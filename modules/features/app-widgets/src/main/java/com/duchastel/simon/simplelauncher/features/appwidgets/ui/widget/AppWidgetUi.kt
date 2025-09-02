package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.host.LauncherAppWidgetHost
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.AppWidgetHostCompose

@Composable
fun AppWidgetUi(
    state: AppWidgetState,
    appWidgetHost: LauncherAppWidgetHost,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            state.isLoading -> {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            state.error != null -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Widget Error",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    
                    Text(
                        text = state.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                    
                    Button(onClick = state.onRetry) {
                        Text("Retry")
                    }
                    
                    Button(
                        onClick = state.onRemoveWidget,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Text("Remove Widget")
                    }
                }
            }
            
            else -> {
                AppWidgetHostCompose(
                    widgetData = state.widgetData,
                    appWidgetHost = appWidgetHost,
                    modifier = Modifier.fillMaxSize(),
                    onError = state.onWidgetError
                )
            }
        }
    }
}