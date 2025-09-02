package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import androidx.compose.runtime.Immutable
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class AppWidgetState(
    val widgetData: WidgetData,
    val isLoading: Boolean = false,
    val error: String? = null,
    val onRetry: () -> Unit = {},
    val onRemoveWidget: () -> Unit = {},
    val onWidgetError: (Throwable) -> Unit = {}
) : CircuitUiState