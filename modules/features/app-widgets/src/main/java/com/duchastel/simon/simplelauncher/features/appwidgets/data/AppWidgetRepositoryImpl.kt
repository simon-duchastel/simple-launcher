package com.duchastel.simon.simplelauncher.features.appwidgets.data

import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ComponentName
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.duchastel.simon.simplelauncher.features.appwidgets.host.LauncherAppWidgetHost
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppWidgetRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val appWidgetHost: LauncherAppWidgetHost,
    private val appWidgetManager: AppWidgetManager
) : AppWidgetRepository {

    override suspend fun getAvailableWidgets(): List<WidgetProviderInfo> {
        return appWidgetManager.installedProviders.map { provider ->
            WidgetProviderInfo(
                componentName = provider.provider.flattenToString(),
                label = provider.loadLabel(context.packageManager),
                previewImage = null, // Could add preview image handling
                minWidth = provider.minWidth,
                minHeight = provider.minHeight,
                maxWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    provider.maxResizeWidth.takeIf { it > 0 }
                } else null,
                maxHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    provider.maxResizeHeight.takeIf { it > 0 }
                } else null,
                resizeMode = provider.resizeMode,
                hasConfigurationActivity = provider.configure != null
            )
        }
    }

    override suspend fun getWidgetDisplayState(selectedWidget: WidgetData?): WidgetDisplayState {
        // If no widget selected, return empty state
        if (selectedWidget == null) {
            return WidgetDisplayState(
                selectedWidget = null,
                boundWidget = null,
                bindingState = BindingState.NotBound,
                error = null
            )
        }
        
        // Check if widget is actually bound to AppWidgetManager
        return try {
            val providerInfo = appWidgetManager.getAppWidgetInfo(selectedWidget.widgetId)
            if (providerInfo != null) {
                // Verify the provider still exists and is valid
                val componentName = android.content.ComponentName.unflattenFromString(selectedWidget.providerComponentName)
                val availableProviders = appWidgetManager.installedProviders
                val providerExists = availableProviders.any { it.provider == componentName }
                
                if (providerExists) {
                    // Widget is bound and valid
                    WidgetDisplayState(
                        selectedWidget = selectedWidget,
                        boundWidget = selectedWidget,
                        bindingState = BindingState.Bound,
                        error = null
                    )
                } else {
                    // Widget is bound but provider was uninstalled - clean up widget ID
                    try {
                        appWidgetHost.deallocateWidgetId(selectedWidget.widgetId)
                    } catch (cleanupError: Exception) {
                        // Ignore cleanup errors, we still want to report the main issue
                    }
                    
                    WidgetDisplayState(
                        selectedWidget = selectedWidget,
                        boundWidget = null,
                        bindingState = BindingState.ProviderNotFound,
                        error = WidgetError.ProviderNotFound
                    )
                }
            } else {
                // Widget was selected but is no longer bound (system cleared it, etc.)
                WidgetDisplayState(
                    selectedWidget = selectedWidget,
                    boundWidget = null,
                    bindingState = BindingState.ProviderNotFound,
                    error = WidgetError.ProviderNotFound
                )
            }
        } catch (e: SecurityException) {
            WidgetDisplayState(
                selectedWidget = selectedWidget,
                boundWidget = null,
                bindingState = BindingState.BindingFailed,
                error = WidgetError.PermissionDenied
            )
        } catch (e: Exception) {
            WidgetDisplayState(
                selectedWidget = selectedWidget,
                boundWidget = null,
                bindingState = BindingState.BindingFailed,
                error = WidgetError.Unknown("Failed to check widget status: ${e.message}")
            )
        }
    }

    override suspend fun allocateWidgetId(): Int {
        return appWidgetHost.allocateWidgetId()
    }

    override suspend fun bindWidget(widgetId: Int, providerInfo: WidgetProviderInfo): WidgetData {
        val componentName = ComponentName.unflattenFromString(providerInfo.componentName)
            ?: throw IllegalArgumentException("Invalid component name")

        val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, componentName)
        
        if (!canBind) {
            throw SecurityException("Widget binding not allowed - need user permission")
        }
        
        return WidgetData(
            widgetId = widgetId,
            providerComponentName = providerInfo.componentName,
            width = providerInfo.minWidth,
            height = providerInfo.minHeight,
            label = providerInfo.label
        )
    }

    override suspend fun unbindWidget(widgetId: Int) {
        // Remove from AppWidgetManager
        appWidgetHost.deallocateWidgetId(widgetId)
    }

    override suspend fun hasConfigurationActivity(providerInfo: WidgetProviderInfo): Boolean {
        return providerInfo.hasConfigurationActivity
    }

    override suspend fun startConfigurationActivity(
        widgetId: Int,
        providerInfo: WidgetProviderInfo
    ) {
        val componentName = ComponentName.unflattenFromString(providerInfo.componentName)
            ?: throw IllegalArgumentException("Invalid component name")

        val providers = appWidgetManager.installedProviders
        val provider = providers.find { it.provider == componentName }
            ?: throw IllegalArgumentException("Provider not found")

        if (provider.configure == null) {
            throw IllegalStateException("No configuration activity")
        }
        
        // Configuration activity exists - would need to start activity for result
        // This would typically be handled by the calling component
        // For now, we just validate that it exists
    }

    override suspend fun getProviderInfo(componentName: String): WidgetProviderInfo? {
        val component = ComponentName.unflattenFromString(componentName) ?: return null
        val provider = appWidgetManager.installedProviders.find { it.provider == component }
            ?: return null

        return WidgetProviderInfo(
            componentName = componentName,
            label = provider.loadLabel(context.packageManager),
            previewImage = null,
            minWidth = provider.minWidth,
            minHeight = provider.minHeight,
            maxWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                provider.maxResizeWidth.takeIf { it > 0 }
            } else null,
            maxHeight = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                provider.maxResizeHeight.takeIf { it > 0 }
            } else null,
            resizeMode = provider.resizeMode,
            hasConfigurationActivity = provider.configure != null
        )
    }

    override suspend fun updateWidgetOptions(widgetId: Int, width: Int, height: Int) {
        val options = Bundle().apply {
            putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, width)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, height)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, width)
            putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, height)
        }
        
        appWidgetManager.updateAppWidgetOptions(widgetId, options)
    }

    override suspend fun createWidgetView(widgetData: WidgetData): AppWidgetHostView {
        val providerInfo = appWidgetManager.getAppWidgetInfo(widgetData.widgetId)
            ?: throw IllegalStateException("Widget provider info not found")
        
        return try {
            appWidgetHost.createView(context, widgetData.widgetId, providerInfo)
        } catch (e: SecurityException) {
            throw SecurityException("Permission denied for widget creation", e)
        } catch (e: Exception) {
            throw IllegalStateException("Failed to create widget host view", e)
        }
    }
}