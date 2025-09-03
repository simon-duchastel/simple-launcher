package com.duchastel.simon.simplelauncher.features.appwidgets.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetProviderInfoTest {

    @Test
    fun `WidgetProviderInfo can be created with minimum required properties`() {
        val providerInfo = WidgetProviderInfo(
            componentName = "com.example/TestWidget",
            label = "Test Widget",
            minWidth = 200,
            minHeight = 100
        )
        
        assertEquals("com.example/TestWidget", providerInfo.componentName)
        assertEquals("Test Widget", providerInfo.label)
        assertNull(providerInfo.previewImage)
        assertEquals(200, providerInfo.minWidth)
        assertEquals(100, providerInfo.minHeight)
        assertNull(providerInfo.maxWidth)
        assertNull(providerInfo.maxHeight)
        assertEquals(0, providerInfo.resizeMode)
        assertFalse(providerInfo.hasConfigurationActivity)
    }

    @Test
    fun `WidgetProviderInfo can be created with all properties`() {
        val providerInfo = WidgetProviderInfo(
            componentName = "com.example/FullWidget",
            label = "Full Widget",
            previewImage = "preview.png",
            minWidth = 200,
            minHeight = 100,
            maxWidth = 400,
            maxHeight = 200,
            resizeMode = 3,
            hasConfigurationActivity = true
        )
        
        assertEquals("com.example/FullWidget", providerInfo.componentName)
        assertEquals("Full Widget", providerInfo.label)
        assertEquals("preview.png", providerInfo.previewImage)
        assertEquals(200, providerInfo.minWidth)
        assertEquals(100, providerInfo.minHeight)
        assertEquals(400, providerInfo.maxWidth)
        assertEquals(200, providerInfo.maxHeight)
        assertEquals(3, providerInfo.resizeMode)
        assertTrue(providerInfo.hasConfigurationActivity)
    }

    @Test
    fun `WidgetProviderInfo objects are equal when all properties match`() {
        val provider1 = WidgetProviderInfo(
            componentName = "com.example/Widget",
            label = "Widget",
            minWidth = 200,
            minHeight = 100
        )
        
        val provider2 = WidgetProviderInfo(
            componentName = "com.example/Widget",
            label = "Widget",
            minWidth = 200,
            minHeight = 100
        )
        
        assertEquals(provider1, provider2)
    }

    @Test
    fun `WidgetProviderInfo objects are not equal when properties differ`() {
        val provider1 = WidgetProviderInfo(
            componentName = "com.example/Widget1",
            label = "Widget 1",
            minWidth = 200,
            minHeight = 100
        )
        
        val provider2 = WidgetProviderInfo(
            componentName = "com.example/Widget2",
            label = "Widget 2",
            minWidth = 200,
            minHeight = 100
        )
        
        assertTrue(provider1 != provider2)
    }

    @Test
    fun `WidgetProviderInfo copy works correctly`() {
        val original = WidgetProviderInfo(
            componentName = "com.example/Original",
            label = "Original Widget",
            minWidth = 200,
            minHeight = 100,
            hasConfigurationActivity = false
        )
        
        val copied = original.copy(
            label = "Copied Widget",
            hasConfigurationActivity = true
        )
        
        assertEquals("com.example/Original", copied.componentName)
        assertEquals("Copied Widget", copied.label)
        assertEquals(200, copied.minWidth)
        assertEquals(100, copied.minHeight)
        assertTrue(copied.hasConfigurationActivity)
    }
}