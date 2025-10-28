package com.duchastel.simon.simplelauncher.features.settings.data

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit

/**
 * A Datastore migration that stores the current version if it's out of date.
 *
 * IMPORTANT: THIS MIGRATION MUST BE RUN AFTER ALL OTHER MIGRATIONS, since other
 * migrations may rely on the version number to determine whether to run or not.
 */
internal object SetDataStoreVersion : DataMigration<Preferences> {
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
