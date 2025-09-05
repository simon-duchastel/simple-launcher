package com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.BindingState
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetProviderInfo
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WidgetSelectionResult {
    data class Selected(val widgetData: WidgetData) : WidgetSelectionResult
    data object Cleared : WidgetSelectionResult
}

class WidgetSelectionPresenter @AssistedInject constructor(
    private val appWidgetRepository: AppWidgetRepository,
    @Assisted private val screen: WidgetSelectionScreen,
    @Assisted private val navigator: Navigator,
) : Presenter<WidgetSelectionState> {

    @Composable
    override fun present(): WidgetSelectionState {
        var availableWidgets by remember { mutableStateOf<List<WidgetProviderInfo>>(emptyList()) }
        var currentWidget by remember { mutableStateOf<WidgetData?>(null) }
        var isLoading by remember { mutableStateOf(true) }
        var error by remember { mutableStateOf<String?>(null) }
        
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            try {
                isLoading = true
                error = null
                
                // Load available widgets
                availableWidgets = appWidgetRepository.getAvailableWidgets()
                
                // Get current widget from screen parameter
                currentWidget = screen.currentWidget
                
                isLoading = false
            } catch (e: Exception) {
                isLoading = false
                error = "Failed to load widgets: ${e.message}"
                currentWidget = null
            }
        }

        return WidgetSelectionState(
            availableWidgets = availableWidgets,
            currentWidget = currentWidget,
            isLoading = isLoading,
            error = error,
            eventSink = { event ->
                when (event) {
                    is WidgetSelectionEvent.SelectWidget -> {
                        // For now, just navigate back - the settings module will handle the binding
                        // In a proper implementation, this would use Circuit's result mechanism
                        navigator.pop()
                    }
                    
                    is WidgetSelectionEvent.ClearWidget -> {
                        // For now, just navigate back - the settings module will handle the clearing
                        // In a proper implementation, this would use Circuit's result mechanism
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
                                // Note: currentWidget will remain as-is since we can't access settings
                                
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


    @AssistedFactory
    fun interface Factory {
        fun create(screen: WidgetSelectionScreen, navigator: Navigator): WidgetSelectionPresenter
    }
}