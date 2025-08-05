package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.slack.circuit.test.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.eq
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class HomepageActionPresenterTest {
    private val smsRepository: SmsRepository = mock()
    private val testDestination = "+1234567890"
    private val testEmoji = "🥳"
    private val config = HomepageActionButton(
        smsDestination = testDestination,
        emoji = testEmoji
    )
    private val presenter = HomepageActionPresenter(config, smsRepository)

    @Test
    fun `state provides configured emoji`() = runTest {
        presenter.test {
            val state = awaitItem()

            assertEquals("🥳", state.emoji)
        }
    }

    @Test
    fun `onClick calls sendSms on repository`() = runTest {
        whenever(smsRepository.sendSms(any(), any())).thenReturn(true)

        presenter.test {
            val state = awaitItem()
            state.onClick.invoke()
            verify(smsRepository).sendSms(eq(testDestination), eq(testEmoji))
        }
    }
}
