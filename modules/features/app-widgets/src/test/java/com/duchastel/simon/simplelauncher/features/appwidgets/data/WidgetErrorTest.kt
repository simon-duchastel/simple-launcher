package com.duchastel.simon.simplelauncher.features.appwidgets.data

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WidgetErrorTest {

    @Test
    fun `ProviderNotFound can be serialized and deserialized`() {
        val error = WidgetError.ProviderNotFound
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.ProviderNotFound)
    }

    @Test
    fun `PermissionDenied can be serialized and deserialized`() {
        val error = WidgetError.PermissionDenied
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.PermissionDenied)
    }

    @Test
    fun `HostCreationFailed can be serialized and deserialized`() {
        val error = WidgetError.HostCreationFailed
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.HostCreationFailed)
    }

    @Test
    fun `WidgetBindingFailed can be serialized and deserialized`() {
        val error = WidgetError.WidgetBindingFailed
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.WidgetBindingFailed)
    }

    @Test
    fun `Unknown error with message can be serialized and deserialized`() {
        val error = WidgetError.Unknown("Custom error message")
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.Unknown)
        assertEquals("Custom error message", (deserialized as WidgetError.Unknown).message)
    }

    @Test
    fun `Unknown error with empty message can be serialized and deserialized`() {
        val error = WidgetError.Unknown("")
        val json = Json.encodeToString(WidgetError.serializer(), error)
        val deserialized = Json.decodeFromString(WidgetError.serializer(), json)
        
        assertEquals(error, deserialized)
        assertTrue(deserialized is WidgetError.Unknown)
        assertEquals("", (deserialized as WidgetError.Unknown).message)
    }

    @Test
    fun `WidgetError objects are equal to themselves`() {
        assertEquals(WidgetError.ProviderNotFound, WidgetError.ProviderNotFound)
        assertEquals(WidgetError.PermissionDenied, WidgetError.PermissionDenied)
        assertEquals(WidgetError.HostCreationFailed, WidgetError.HostCreationFailed)
        assertEquals(WidgetError.WidgetBindingFailed, WidgetError.WidgetBindingFailed)
        assertEquals(WidgetError.Unknown("test"), WidgetError.Unknown("test"))
    }

    @Test
    fun `Unknown errors with different messages are not equal`() {
        val error1 = WidgetError.Unknown("message1")
        val error2 = WidgetError.Unknown("message2")
        
        assertTrue(error1 != error2)
    }
}