package com.duchastel.simon.simplelauncher.features.settings.data

import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user settings.
 * Provides methods to read and write settings data.
 */
interface SettingsRepository {

    /**
     * Saves the given [settingData]. Returns true when the save was successful, false otherwise.
     */
    suspend fun saveSetting(settingData: SettingData): Boolean

    /**
     * Gets the data for the current, or null if the setting isn't stored.
     */
    fun getSettingsFlow(setting: Setting): Flow<SettingData?>
}
