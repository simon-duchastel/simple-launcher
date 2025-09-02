package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.AppWidgetHostCompose
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.WidgetErrorView

@Composable
fun AppWidgetUi(
    state: AppWidgetState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (state.widgetViewState) {
            is WidgetViewState.Error -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    WidgetErrorView(
                        error = state.widgetViewState.error,
                        modifier = Modifier.fillMaxSize().weight(1f)
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
                    widgetViewState = state.widgetViewState,
                    widgetHostView = state.widgetHostView,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}