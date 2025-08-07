package com.duchastel.simon.simplelauncher.features.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsRepositoryImplTest {

    private lateinit var testDataStore: DataStore<Preferences>
    private lateinit var settingsRepository: SettingsRepositoryImpl
    private lateinit var tmpFile: File

    @Before
    fun setUp() {
        tmpFile = File.createTempFile("test", "test")
        testDataStore = PreferenceDataStoreFactory.create(
            produceFile = { tmpFile },
            migrations = listOf(SetDataStoreVersion)
        )
        settingsRepository = SettingsRepositoryImpl(testDataStore)
    }

    @After
    fun tearDown() {
        tmpFile.delete()
    }

    @Test
    fun `getSettingsFlow returns null when no setting is saved`() = runTest {
        val setting = settingsRepository.getSettingsFlow(Setting.HomepageAction)?.first()
        assertNull(setting)
    }

    @Test
    fun `saveSetting and getSettingsFlow work correctly`() = runTest {
        val homepageAction = SettingData.HomepageActionSettingData("👍", "1234567890")
        val saveResult = settingsRepository.saveSetting(homepageAction)
        assertTrue(saveResult)

        val setting = settingsRepository.getSettingsFlow(Setting.HomepageAction)?.first()
        assertEquals(homepageAction, setting)
    }

    @Test
    fun `migration runs correctly`() = runTest {
        // Create a datastore with no version
        testDataStore.edit { prefs ->
            prefs.clear()
        }

        // Accessing the data should trigger the migration
        val setting = settingsRepository.getSettingsFlow(Setting.HomepageAction)?.first()
        assertNull(setting)

        // Check that the version is now set
        val version = testDataStore.data.first()[PreferenceKeys.SCHEMA_VERSION]
        assertEquals(1, version)
    }
}
