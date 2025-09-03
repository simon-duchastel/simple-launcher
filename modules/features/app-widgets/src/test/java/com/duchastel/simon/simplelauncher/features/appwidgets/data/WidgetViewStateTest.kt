package com.duchastel.simon.simplelauncher.features.appwidgets.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetViewStateTest {

    @Test
    fun `Loading state can be serialized and deserialized`() {
        val state = WidgetViewState.Loading
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Loading)
    }

    @Test
    fun `Success state can be serialized and deserialized`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/ExampleWidget",
            width = 200,
            height = 100
        )
        val state = WidgetViewState.Success(widgetData)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Success)
        assertEquals(widgetData, (deserialized as WidgetViewState.Success).widgetData)
    }

    @Test
    fun `Error state with ProviderNotFound can be serialized and deserialized`() {
        val error = WidgetError.ProviderNotFound
        val state = WidgetViewState.Error(error)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Error)
        assertEquals(error, (deserialized as WidgetViewState.Error).error)
    }

    @Test
    fun `Error state with PermissionDenied can be serialized and deserialized`() {
        val error = WidgetError.PermissionDenied
        val state = WidgetViewState.Error(error)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Error)
        assertEquals(error, (deserialized as WidgetViewState.Error).error)
    }

    @Test
    fun `Error state with HostCreationFailed can be serialized and deserialized`() {
        val error = WidgetError.HostCreationFailed
        val state = WidgetViewState.Error(error)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Error)
        assertEquals(error, (deserialized as WidgetViewState.Error).error)
    }

    @Test
    fun `Error state with WidgetBindingFailed can be serialized and deserialized`() {
        val error = WidgetError.WidgetBindingFailed
        val state = WidgetViewState.Error(error)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Error)
        assertEquals(error, (deserialized as WidgetViewState.Error).error)
    }

    @Test
    fun `Error state with Unknown error can be serialized and deserialized`() {
        val error = WidgetError.Unknown("Custom error message")
        val state = WidgetViewState.Error(error)
        val json = Json.encodeToString(WidgetViewState.serializer(), state)
        val deserialized = Json.decodeFromString(WidgetViewState.serializer(), json)
        
        assertEquals(state, deserialized)
        assertTrue(deserialized is WidgetViewState.Error)
        val deserializedError = (deserialized as WidgetViewState.Error).error
        assertTrue(deserializedError is WidgetError.Unknown)
        assertEquals("Custom error message", (deserializedError as WidgetError.Unknown).message)
    }

    @Test
    fun `WidgetViewState objects are equal to themselves`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/ExampleWidget",
            width = 200,
            height = 100
        )
        assertEquals(WidgetViewState.Loading, WidgetViewState.Loading)
        assertEquals(WidgetViewState.Success(widgetData), WidgetViewState.Success(widgetData))
        assertEquals(
            WidgetViewState.Error(WidgetError.ProviderNotFound),
            WidgetViewState.Error(WidgetError.ProviderNotFound)
        )
    }

    @Test
    fun `Error states with different errors are not equal`() {
        val errorState1 = WidgetViewState.Error(WidgetError.ProviderNotFound)
        val errorState2 = WidgetViewState.Error(WidgetError.PermissionDenied)
        
        assertTrue(errorState1 != errorState2)
    }

    @Test
    fun `Different state types are not equal`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/ExampleWidget",
            width = 200,
            height = 100
        )
        assertTrue(WidgetViewState.Loading != WidgetViewState.Success(widgetData))
        assertTrue(WidgetViewState.Loading != WidgetViewState.Error(WidgetError.ProviderNotFound))
        assertTrue(WidgetViewState.Success(widgetData) != WidgetViewState.Error(WidgetError.ProviderNotFound))
    }
}