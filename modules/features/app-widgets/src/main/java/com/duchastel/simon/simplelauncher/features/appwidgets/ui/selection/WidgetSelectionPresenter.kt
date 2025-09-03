package com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetProviderInfo
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WidgetSelectionResult {
    data class Selected(val widgetData: WidgetData) : WidgetSelectionResult
    data object Cleared : WidgetSelectionResult
}

class WidgetSelectionPresenter @Inject constructor(
    private val appWidgetRepository: AppWidgetRepository,
    private val navigator: Navigator,
) : Presenter<WidgetSelectionState> {

    @Composable
    override fun present(): WidgetSelectionState {
        var availableWidgets by remember { mutableStateOf<List<WidgetProviderInfo>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }
        
        val scope = rememberCoroutineScope()

        remember {
            scope.launch {
                try {
                    isLoading = true
                    error = null
                    
                    // Load available widgets
                    availableWidgets = appWidgetRepository.getAvailableWidgets()
                    
                    isLoading = false
                } catch (e: Exception) {
                    isLoading = false
                    error = "Failed to load widgets: ${e.message}"
                }
            }
        }

        return WidgetSelectionState(
            availableWidgets = availableWidgets,
            currentWidget = null, // Will be provided by settings module
            isLoading = isLoading,
            error = error,
            eventSink = { event ->
                when (event) {
                    is WidgetSelectionEvent.SelectWidget -> {
                        scope.launch {
                            try {
                                // Allocate and bind new widget
                                val widgetId = appWidgetRepository.allocateWidgetId()
                                val result = appWidgetRepository.bindWidget(widgetId, event.providerInfo)
                                
                                result.fold(
                                    onSuccess = {
                                        // Create widget data
                                        val widgetData = WidgetData(
                                            widgetId = widgetId,
                                            providerComponentName = event.providerInfo.componentName,
                                            width = event.providerInfo.minWidth,
                                            height = event.providerInfo.minHeight,
                                            label = event.providerInfo.label
                                        )
                                        
                                        // For now, just navigate back - result handling will be done differently
                                        navigator.pop()
                                    },
                                    onFailure = { exception ->
                                        error = "Failed to configure widget: ${exception.message}"
                                    }
                                )
                            } catch (e: Exception) {
                                error = "Failed to configure widget: ${e.message}"
                            }
                        }
                    }
                    
                    is WidgetSelectionEvent.ClearWidget -> {
                        // For now, just navigate back - result handling will be done differently
                        navigator.pop()
                    }
                    
                    is WidgetSelectionEvent.NavigateBack -> {
                        navigator.pop()
                    }
                    
                    is WidgetSelectionEvent.Retry -> {
                        scope.launch {
                            try {
                                isLoading = true
                                error = null
                                availableWidgets = appWidgetRepository.getAvailableWidgets()
                                isLoading = false
                            } catch (e: Exception) {
                                isLoading = false
                                error = "Failed to load widgets: ${e.message}"
                            }
                        }
                    }
                }
            }
        )
    }
}