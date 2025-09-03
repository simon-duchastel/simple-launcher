package com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetProviderInfo
import com.slack.circuit.runtime.CircuitUiState

data class WidgetSelectionState(
    val availableWidgets: List<WidgetProviderInfo>,
    val currentWidget: WidgetData?,
    val isLoading: Boolean,
    val error: String?,
    val eventSink: (WidgetSelectionEvent) -> Unit
) : CircuitUiState

sealed interface WidgetSelectionEvent {
    data class SelectWidget(val providerInfo: WidgetProviderInfo) : WidgetSelectionEvent
    data object ClearWidget : WidgetSelectionEvent
    data object NavigateBack : WidgetSelectionEvent
    data object Retry : WidgetSelectionEvent
}