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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppWidgetRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val appWidgetHost: LauncherAppWidgetHost,
    private val appWidgetManager: AppWidgetManager
) : AppWidgetRepository {

    private val _boundWidgets = MutableStateFlow<List<WidgetData>>(emptyList())
    private val _widgetViewStates = MutableStateFlow<Map<Int, WidgetViewState>>(emptyMap())

    override fun getAvailableWidgets(): List<WidgetProviderInfo> {
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

    override fun getBoundWidgets(): Flow<List<WidgetData>> {
        return _boundWidgets.asStateFlow()
    }

    override suspend fun allocateWidgetId(): Int {
        return appWidgetHost.allocateWidgetId()
    }

    override suspend fun bindWidget(widgetId: Int, providerInfo: WidgetProviderInfo): Result<Unit> {
        return try {
            val componentName = ComponentName.unflattenFromString(providerInfo.componentName)
                ?: return Result.failure(IllegalArgumentException("Invalid component name"))

            val canBind = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, componentName)
            
            if (canBind) {
                val widgetData = WidgetData(
                    widgetId = widgetId,
                    providerComponentName = providerInfo.componentName,
                    width = providerInfo.minWidth,
                    height = providerInfo.minHeight,
                    label = providerInfo.label
                )
                
                val currentWidgets = _boundWidgets.value.toMutableList()
                currentWidgets.add(widgetData)
                _boundWidgets.value = currentWidgets
                
                Result.success(Unit)
            } else {
                // Need to request permission from user
                Result.failure(SecurityException("Widget binding not allowed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun removeWidget(widgetId: Int): Result<Unit> {
        return try {
            appWidgetHost.deallocateWidgetId(widgetId)
            
            val currentWidgets = _boundWidgets.value.toMutableList()
            currentWidgets.removeAll { it.widgetId == widgetId }
            _boundWidgets.value = currentWidgets
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun hasConfigurationActivity(providerInfo: WidgetProviderInfo): Boolean {
        return providerInfo.hasConfigurationActivity
    }

    override suspend fun startConfigurationActivity(
        widgetId: Int,
        providerInfo: WidgetProviderInfo
    ): Result<Unit> {
        return try {
            val componentName = ComponentName.unflattenFromString(providerInfo.componentName)
                ?: return Result.failure(IllegalArgumentException("Invalid component name"))

            val providers = appWidgetManager.installedProviders
            val provider = providers.find { it.provider == componentName }
                ?: return Result.failure(IllegalArgumentException("Provider not found"))

            if (provider.configure != null) {
                // Configuration activity exists - would need to start activity for result
                // This would typically be handled by the calling component
                Result.success(Unit)
            } else {
                Result.failure(IllegalStateException("No configuration activity"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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

    override suspend fun updateWidgetOptions(widgetId: Int, width: Int, height: Int): Result<Unit> {
        return try {
            val options = Bundle().apply {
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, height)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, width)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, height)
            }
            
            appWidgetManager.updateAppWidgetOptions(widgetId, options)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createWidgetView(widgetData: WidgetData): Result<AppWidgetHostView> {
        return try {
            updateWidgetViewState(widgetData.widgetId, WidgetViewState.Loading)
            
            val providerInfo = appWidgetManager.getAppWidgetInfo(widgetData.widgetId)
            if (providerInfo == null) {
                updateWidgetViewState(widgetData.widgetId, WidgetViewState.Error(WidgetError.ProviderNotFound))
                return Result.failure(IllegalStateException("Widget provider info not found"))
            }
            
            val hostView = appWidgetHost.createView(context, widgetData.widgetId, providerInfo)
            updateWidgetViewState(widgetData.widgetId, WidgetViewState.Success(widgetData))
            
            Result.success(hostView)
        } catch (e: SecurityException) {
            updateWidgetViewState(widgetData.widgetId, WidgetViewState.Error(WidgetError.PermissionDenied))
            Result.failure(e)
        } catch (e: Exception) {
            updateWidgetViewState(widgetData.widgetId, WidgetViewState.Error(WidgetError.HostCreationFailed))
            Result.failure(e)
        }
    }

    override fun getWidgetViewState(widgetId: Int): Flow<WidgetViewState> {
        return _widgetViewStates.map { states ->
            states[widgetId] ?: WidgetViewState.Loading
        }
    }
    
    private fun updateWidgetViewState(widgetId: Int, state: WidgetViewState) {
        val currentStates = _widgetViewStates.value.toMutableMap()
        currentStates[widgetId] = state
        _widgetViewStates.value = currentStates
    }
}