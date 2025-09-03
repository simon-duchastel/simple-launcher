package com.duchastel.simon.simplelauncher.features.appwidgets.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AppWidgetRepositorySimpleTest {

    @Test
    fun `WidgetData can be created successfully`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/TestWidget",
            width = 200,
            height = 100,
            label = "Test Widget"
        )
        
        assertEquals(123, widgetData.widgetId)
        assertEquals("com.example/TestWidget", widgetData.providerComponentName)
        assertEquals(200, widgetData.width)
        assertEquals(100, widgetData.height)
        assertEquals("Test Widget", widgetData.label)
    }

    @Test
    fun `WidgetProviderInfo can be created successfully`() {
        val providerInfo = WidgetProviderInfo(
            componentName = "com.example/TestWidget",
            label = "Test Widget",
            minWidth = 200,
            minHeight = 100,
            hasConfigurationActivity = true
        )
        
        assertEquals("com.example/TestWidget", providerInfo.componentName)
        assertEquals("Test Widget", providerInfo.label)
        assertEquals(200, providerInfo.minWidth)
        assertEquals(100, providerInfo.minHeight)
        assertEquals(true, providerInfo.hasConfigurationActivity)
    }

    @Test
    fun `WidgetViewState can be Loading`() {
        val state = WidgetViewState.Loading
        assertEquals(WidgetViewState.Loading, state)
    }

    @Test
    fun `WidgetViewState can be Success with data`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/TestWidget",
            width = 200,
            height = 100
        )
        val state = WidgetViewState.Success(widgetData)
        assertEquals(widgetData, (state as WidgetViewState.Success).widgetData)
    }

    @Test
    fun `WidgetViewState can be Error with specific error types`() {
        val providerNotFoundState = WidgetViewState.Error(WidgetError.ProviderNotFound)
        val permissionDeniedState = WidgetViewState.Error(WidgetError.PermissionDenied)
        val hostCreationFailedState = WidgetViewState.Error(WidgetError.HostCreationFailed)
        val bindingFailedState = WidgetViewState.Error(WidgetError.WidgetBindingFailed)
        val unknownErrorState = WidgetViewState.Error(WidgetError.Unknown("Custom message"))
        
        assertEquals(WidgetError.ProviderNotFound, (providerNotFoundState as WidgetViewState.Error).error)
        assertEquals(WidgetError.PermissionDenied, (permissionDeniedState as WidgetViewState.Error).error)
        assertEquals(WidgetError.HostCreationFailed, (hostCreationFailedState as WidgetViewState.Error).error)
        assertEquals(WidgetError.WidgetBindingFailed, (bindingFailedState as WidgetViewState.Error).error)
        assertEquals("Custom message", ((unknownErrorState as WidgetViewState.Error).error as WidgetError.Unknown).message)
    }

    @Test
    fun `AppWidgetState can be created with all properties`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/TestWidget",
            width = 200,
            height = 100
        )
        
        var retryPressed = false
        var removePressed = false
        
        val state = com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Loading,
            widgetHostView = null,
            onRetry = { retryPressed = true },
            onRemoveWidget = { removePressed = true }
        )
        
        assertEquals(widgetData, state.widgetData)
        assertEquals(WidgetViewState.Loading, state.widgetViewState)
        assertEquals(null, state.widgetHostView)
        
        // Test callbacks
        state.onRetry()
        assertEquals(true, retryPressed)
        
        state.onRemoveWidget()
        assertEquals(true, removePressed)
    }
}