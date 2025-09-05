package com.duchastel.simon.simplelauncher.features.appwidgets.data

import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetProviderInfo

interface AppWidgetRepository {
    
    /**
     * Gets all available widget providers on the device
     */
    suspend fun getAvailableWidgets(): List<WidgetProviderInfo>
    
    /**
     * Gets current widget display state based on provided widget data and AppWidgetManager
     * This combines user preferences with actual binding state
     */
    suspend fun getWidgetDisplayState(selectedWidget: WidgetData?): WidgetDisplayState
    
    /**
     * Allocates a new widget ID
     */
    suspend fun allocateWidgetId(): Int
    
    /**
     * Binds a widget to the launcher
     */
    suspend fun bindWidget(widgetId: Int, providerInfo: WidgetProviderInfo): WidgetData
    
    /**
     * Removes a widget from the launcher
     */
    suspend fun unbindWidget(widgetId: Int)
    
    /**
     * Checks if a widget has a configuration activity
     */
    suspend fun hasConfigurationActivity(providerInfo: WidgetProviderInfo): Boolean
    
    /**
     * Starts the configuration activity for a widget
     */
    suspend fun startConfigurationActivity(widgetId: Int, providerInfo: WidgetProviderInfo)
    
    /**
     * Gets widget provider info for a given component name
     */
    suspend fun getProviderInfo(componentName: String): WidgetProviderInfo?
    
    /**
     * Updates widget options (size, etc.)
     */
    suspend fun updateWidgetOptions(widgetId: Int, width: Int, height: Int)
    
    /**
     * Creates a widget host view for the given widget data
     */
    suspend fun createWidgetView(widgetData: WidgetData): AppWidgetHostView
}