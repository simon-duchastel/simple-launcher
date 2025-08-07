package com.duchastel.simon.simplelauncher.features.settings.data

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Keys used to store top-level data in this preferences store
 */
internal object PreferenceKeys {
    /**
     * Current schema version
     */
    val SCHEMA_VERSION = intPreferencesKey("version")

    /**
     *
     */
    val HOMEPAGE_ACTION = stringPreferencesKey("homepage_action")
}
