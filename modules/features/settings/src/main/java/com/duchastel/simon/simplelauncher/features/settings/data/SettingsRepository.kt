package com.duchastel.simon.simplelauncher.features.settings.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//data class Settings(
//    val username: String,
//    val notificationsEnabled: Boolean,
//    val fontSize: Int,
//    val favoriteChar: Char
//)
//
//// 2. Preference keys object
//object PreferenceKeys {
//    val USERNAME = stringPreferencesKey("username")
//    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
//    val FONT_SIZE = intPreferencesKey("font_size")
//    val FAVORITE_CHAR = stringPreferencesKey("favorite_char") // Stored as String
//}
//
//// DataStore instance for user settings
//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")
//
//// 3. SettingsRepository implementation
//class SettingsRepository(private val context: Context) {
//
//    val settingsFlow: Flow<Settings> = context.dataStore.data
//        .map { preferences ->
//            Settings(
//                username = preferences[PreferenceKeys.USERNAME] ?: "",
//                notificationsEnabled = preferences[PreferenceKeys.NOTIFICATIONS_ENABLED] ?: false,
//                fontSize = preferences[PreferenceKeys.FONT_SIZE] ?: 14,
//                favoriteChar = (preferences[PreferenceKeys.FAVORITE_CHAR]?.firstOrNull() ?: 'A')
//            )
//        }
//
//    suspend fun updateSettings(newSettings: Settings) {
//        context.dataStore.edit { preferences ->
//            preferences[PreferenceKeys.USERNAME] = newSettings.username
//            preferences[PreferenceKeys.NOTIFICATIONS_ENABLED] = newSettings.notificationsEnabled
//            preferences[PreferenceKeys.FONT_SIZE] = newSettings.fontSize
//            preferences[PreferenceKeys.FAVORITE_CHAR] = newSettings.favoriteChar.toString()
//        }
//    }
//}

/*
// 4. Usage example snippets (for ViewModel)

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsRepository: SettingsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(Settings(
        username = "",
        notificationsEnabled = false,
        fontSize = 14,
        favoriteChar = 'A'
    ))
    val uiState: StateFlow<Settings> = _uiState.asStateFlow()

    init {
        // Observe settingsFlow and update UI state
        settingsRepository.settingsFlow
            .onEach { settings ->
                _uiState.value = settings
            }
            .launchIn(viewModelScope) // Collects the flow in the viewModelScope
    }

    // Function to update settings from UI
    fun updateUsername(username: String) {
        viewModelScope.launch {
            settingsRepository.updateSettings(uiState.value.copy(username = username))
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.updateSettings(uiState.value.copy(notificationsEnabled = enabled))
        }
    }

    fun updateFontSize(fontSize: Int) {
        viewModelScope.launch {
            settingsRepository.updateSettings(uiState.value.copy(fontSize = fontSize))
        }
    }

    fun updateFavoriteChar(char: Char) {
        viewModelScope.launch {
            settingsRepository.updateSettings(uiState.value.copy(favoriteChar = char))
        }
    }
}

*/

/**
 * Repository interface for managing user settings.
 * Provides methods to read and write settings data.
 */
interface SettingsRepository {

    fun writeSetting(): Boolean

    fun getSettingsFlow(): Flow<Setting>
}
