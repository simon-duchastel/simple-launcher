package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import androidx.compose.runtime.Immutable
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.slack.circuit.runtime.CircuitUiState

@Immutable
data class AppWidgetState(
    val widgetData: WidgetData,
    val widgetViewState: WidgetViewState,
    val widgetHostView: AppWidgetHostView?,
    val onRetry: () -> Unit,
    val onRemoveWidget: () -> Unit
) : CircuitUiState