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
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

class WidgetSelectionPresenter @Inject constructor(
    private val appWidgetRepository: AppWidgetRepository,
    private val settingsRepository: SettingsRepository,
    private val navigator: Navigator,
) : Presenter<WidgetSelectionState> {

    @Composable
    override fun present(): WidgetSelectionState {
        var availableWidgets by remember { mutableStateOf<List<WidgetProviderInfo>>(emptyList()) }
        var currentWidget by remember { mutableStateOf<WidgetData?>(null) }
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
                    
                    // Load current widget selection
                    val settingData = settingsRepository.getSettingsFlow(Setting.WidgetConfiguration)?.first()
                    currentWidget = (settingData as? SettingData.WidgetConfigurationSettingData)?.widgetData
                    
                    isLoading = false
                } catch (e: Exception) {
                    isLoading = false
                    error = "Failed to load widgets: ${e.message}"
                }
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
                        scope.launch {
                            try {
                                // Allocate and bind new widget
                                val widgetId = appWidgetRepository.allocateWidgetId()
                                val result = appWidgetRepository.bindWidget(widgetId, event.providerInfo)
                                
                                result.fold(
                                    onSuccess = {
                                        // Create widget data and save to settings
                                        val widgetData = WidgetData(
                                            widgetId = widgetId,
                                            providerComponentName = event.providerInfo.componentName,
                                            width = event.providerInfo.minWidth,
                                            height = event.providerInfo.minHeight,
                                            label = event.providerInfo.label
                                        )
                                        
                                        // Remove previous widget if exists
                                        currentWidget?.let { oldWidget ->
                                            appWidgetRepository.removeWidget(oldWidget.widgetId)
                                        }
                                        
                                        // Save new widget selection
                                        val settingData = SettingData.WidgetConfigurationSettingData(widgetData)
                                        settingsRepository.saveSetting(settingData)
                                        
                                        currentWidget = widgetData
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
                        scope.launch {
                            try {
                                // Remove current widget
                                currentWidget?.let { widget ->
                                    appWidgetRepository.removeWidget(widget.widgetId)
                                }
                                
                                // Clear widget selection
                                val settingData = SettingData.WidgetConfigurationSettingData(null)
                                settingsRepository.saveSetting(settingData)
                                
                                currentWidget = null
                                navigator.pop()
                            } catch (e: Exception) {
                                error = "Failed to clear widget: ${e.message}"
                            }
                        }
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