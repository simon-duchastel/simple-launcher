package com.duchastel.simon.simplelauncher.features.settings.data

import org.junit.Before
import org.junit.Test

class SettingsRepositoryImplTest {

    private lateinit var settingsRepository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        settingsRepository = SettingsRepositoryImpl()
    }

    @Test
    fun `SettingsRepositoryImpl can be instantiated`() {
        // Currently no logic to test, but ensuring it can be instantiated
        // without errors.
    }
}
