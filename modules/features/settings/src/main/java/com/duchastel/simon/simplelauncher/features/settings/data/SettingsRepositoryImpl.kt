package com.duchastel.simon.simplelauncher.features.settings.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SettingsRepositoryImpl @Inject internal constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    override fun getSettingsFlow(setting: Setting): Flow<SettingData?>? {
        return try {
            dataStore.data.map { preferences ->
                val settingString = preferences[setting.preferenceKey()]
                if (settingString == null) {
                    // we got a result successfully, and that result is that no data exists
                    return@map null
                }

                return@map Json.decodeFromString<SettingData>(settingString)
            }
        } catch (_: IOException) {
            // if we receive an error reading the settings, return false
            return null
        } catch (_: SerializationException) {
            // if we can't deserialize, return an error
            return null
        } catch (_: IllegalArgumentException) {
            // if we can deserialize but the data is invalid, return an error
            return null
        }
    }

    override suspend fun saveSetting(settingData: SettingData): Boolean {
        try {
            dataStore.edit { preferences ->
                val encodedData = Json.encodeToString(settingData)
                preferences[settingData.toSetting().preferenceKey()] = encodedData
            }
            return true
        } catch (_: IOException) {
            // if we receive an error editing the file, return false
            return false
        }
    }

    private fun Setting.preferenceKey(): Preferences.Key<String> {
        return when (this) {
            Setting.HomepageAction -> PreferenceKeys.HOMEPAGE_ACTION
        }
    }
}