package com.duchastel.simon.simplelauncher.features.settings.data

import android.content.Context
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class SettingsRepositoryImplTest {

    private lateinit var settingsRepository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        settingsRepository = SettingsRepositoryImpl(mock(Context::class.java))
    }

    @Test
    fun `SettingsRepositoryImpl can be instantiated`() {
        // Currently no logic to test, but ensuring it can be instantiated
        // without errors.
    }
}
