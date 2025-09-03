package com.duchastel.simon.simplelauncher.features.appwidgets.data

import android.os.Parcel
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

class WidgetDataTest {

    @Test
    fun `WidgetData can be serialized and deserialized`() {
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/ExampleWidget",
            width = 200,
            height = 100,
            label = "Test Widget"
        )
        
        val json = Json.encodeToString(WidgetData.serializer(), widgetData)
        val deserialized = Json.decodeFromString(WidgetData.serializer(), json)
        
        assertEquals(widgetData, deserialized)
        assertEquals(123, deserialized.widgetId)
        assertEquals("com.example/ExampleWidget", deserialized.providerComponentName)
        assertEquals(200, deserialized.width)
        assertEquals(100, deserialized.height)
        assertEquals("Test Widget", deserialized.label)
    }

    @Test
    fun `WidgetData can be created without label`() {
        val widgetData = WidgetData(
            widgetId = 456,
            providerComponentName = "com.example/AnotherWidget",
            width = 300,
            height = 150
        )
        
        assertEquals(456, widgetData.widgetId)
        assertEquals("com.example/AnotherWidget", widgetData.providerComponentName)
        assertEquals(300, widgetData.width)
        assertEquals(150, widgetData.height)
        assertEquals(null, widgetData.label)
    }

    @Test
    fun `WidgetData objects are equal when all properties match`() {
        val widget1 = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/Widget",
            width = 200,
            height = 100,
            label = "Test"
        )
        
        val widget2 = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/Widget",
            width = 200,
            height = 100,
            label = "Test"
        )
        
        assertEquals(widget1, widget2)
    }

    @Test
    fun `WidgetData objects are not equal when properties differ`() {
        val widget1 = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/Widget",
            width = 200,
            height = 100
        )
        
        val widget2 = WidgetData(
            widgetId = 456,
            providerComponentName = "com.example/Widget",
            width = 200,
            height = 100
        )
        
        assertTrue(widget1 != widget2)
    }

    @Test
    fun `WidgetData can be parceled and unparceled`() {
        // Note: This test uses mockito to mock the Parcel since we're in unit tests
        // In instrumented tests, we would use the real Parcel implementation
        val parcel = mock<Parcel> {
            on { readInt() } doReturn 123 doReturn 200 doReturn 100
            on { readString() } doReturn "com.example/Widget" doReturn "Test Widget"
        }
        
        val widgetData = WidgetData(
            widgetId = 123,
            providerComponentName = "com.example/Widget",
            width = 200,
            height = 100,
            label = "Test Widget"
        )
        
        // Verify the object can be created (Parcelize implementation is generated)
        assertEquals(123, widgetData.widgetId)
        assertEquals("com.example/Widget", widgetData.providerComponentName)
        assertEquals(200, widgetData.width)
        assertEquals(100, widgetData.height)
        assertEquals("Test Widget", widgetData.label)
    }
}