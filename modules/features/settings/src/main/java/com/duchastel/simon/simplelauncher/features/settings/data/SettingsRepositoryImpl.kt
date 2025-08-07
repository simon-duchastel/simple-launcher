package com.duchastel.simon.simplelauncher.features.settings.data

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.duchastel.simon.simplelauncher.features.settings.data.PreferenceKeys.HOMEPAGE_ACTION
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData.HomepageActionSettingData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "SettingsRepositoryImpl",
    produceMigrations = { listOf(SetDataStoreVersion) }
)

class SettingsRepositoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context,
) : SettingsRepository {

    override fun getSettingsFlow(setting: Setting): Flow<SettingData> {
        return context.dataStore.data.map { preferences ->
            val setting = preferences[setting.preferenceKey()]
            HomepageActionSettingData(
                emoji = "😘",
                phoneNumber = "",
            )
        }
    }

    override suspend fun saveSetting(settingData: SettingData): Boolean {
        try {
            context.dataStore.edit { preferences ->
                preferences[settingData.toSetting().preferenceKey()] = settingData.toString()
            }
            return true
        } catch (_: IOException) {
            // if we receive an error editing the file, return false
            return false
        }
    }

    private fun Setting.preferenceKey(): Preferences.Key<String> {
        return when (this) {
            Setting.HomepageAction -> HOMEPAGE_ACTION
        }
    }
}

/**
 * A Datastore migration that stores the current version if it's out of date.
 *
 * IMPORTANT: THIS MIGRATION MUST BE RUN AFTER ALL OTHER MIGRATIONS, since other
 * migrations may rely on the version number to determine whether to run or not.
 */
private object SetDataStoreVersion : DataMigration<Preferences> {
    /**
     * Returns true when this [Preferences] datastore doesn't contain a version OR that version is
     * out of date, meaning we need to add one
     */
    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        return currentData.isOutOfDate()
    }

    /**
     * Migrates the DataStore to add the current version in order to make migrations easier
     */
    override suspend fun migrate(currentData: Preferences): Preferences {
        return currentData.toMutablePreferences().apply {
            set(PreferenceKeys.SCHEMA_VERSION, CURRENT_SCHEMA_VERSION)
        }
    }

    /**
     * No cleanup is needed, so this override is empty
     */
    override suspend fun cleanUp() {
        // No cleanup necessary
    }
}

/**
 * Returns true when this [Preferences]'s schema is out of date, meaning the stored version
 * either doesn't exist or is less than the current version this source code expects.
 * Returns false otherwise.
 */
private fun Preferences.isOutOfDate(): Boolean {
    val currentVersion = get(PreferenceKeys.SCHEMA_VERSION)
    return currentVersion == null || currentVersion < CURRENT_SCHEMA_VERSION
}

/**
 * Current version of this settings DataStore
 */
private const val CURRENT_SCHEMA_VERSION = 1

/**
 * Keys used to store top-level data in this preferences store
 */
private object PreferenceKeys {
    /**
     * Current schema version
     */
    val SCHEMA_VERSION = intPreferencesKey("version")

    /**
     *
     */
    val HOMEPAGE_ACTION = stringPreferencesKey("homepage_action")
}