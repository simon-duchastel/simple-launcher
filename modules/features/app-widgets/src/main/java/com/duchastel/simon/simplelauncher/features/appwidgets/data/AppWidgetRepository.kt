package com.duchastel.simon.simplelauncher.features.appwidgets.data

import android.appwidget.AppWidgetProviderInfo
import kotlinx.coroutines.flow.Flow

interface AppWidgetRepository {
    
    /**
     * Gets all available widget providers on the device
     */
    fun getAvailableWidgets(): List<WidgetProviderInfo>
    
    /**
     * Gets currently bound widgets
     */
    fun getBoundWidgets(): Flow<List<WidgetData>>
    
    /**
     * Allocates a new widget ID
     */
    suspend fun allocateWidgetId(): Int
    
    /**
     * Binds a widget to the launcher
     */
    suspend fun bindWidget(widgetId: Int, providerInfo: WidgetProviderInfo): Result<Unit>
    
    /**
     * Removes a widget from the launcher
     */
    suspend fun removeWidget(widgetId: Int): Result<Unit>
    
    /**
     * Checks if a widget has a configuration activity
     */
    suspend fun hasConfigurationActivity(providerInfo: WidgetProviderInfo): Boolean
    
    /**
     * Starts the configuration activity for a widget
     */
    suspend fun startConfigurationActivity(widgetId: Int, providerInfo: WidgetProviderInfo): Result<Unit>
    
    /**
     * Gets widget provider info for a given component name
     */
    suspend fun getProviderInfo(componentName: String): WidgetProviderInfo?
    
    /**
     * Updates widget options (size, etc.)
     */
    suspend fun updateWidgetOptions(widgetId: Int, width: Int, height: Int): Result<Unit>
}