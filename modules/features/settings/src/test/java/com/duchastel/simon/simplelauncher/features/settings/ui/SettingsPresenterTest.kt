package com.duchastel.simon.simplelauncher.features.settings.ui

import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.test
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsPresenterTest {

    private val repository: SettingsRepository = mock()
    private val navigator: FakeNavigator = FakeNavigator(SettingsScreen)

    private val presenter = SettingsPresenter(navigator, repository)

    @Test
    fun `presenter returns all settings rows`() = runTest {
        presenter.test {
            val state = awaitItem()

            val expected = listOf(
                SettingsState.SettingsRow.HOMEPAGE_ACTION,
            ).toImmutableList()
            assertEquals(expected, state.settingsRows)
        }
    }
}
