package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import android.content.Intent
import android.net.Uri
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.slack.circuit.test.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class HomepageActionPresenterTest {
    private val smsRepository: SmsRepository = mock()
    private val intentLauncher: IntentLauncher = mock()
    private val testDestination = "+1234567890"
    private val testEmoji = "🥳"
    private val config = HomepageActionButton(
        smsDestination = testDestination,
        emoji = testEmoji
    )
    private val presenter = HomepageActionPresenter(config, smsRepository, intentLauncher)

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

    @Ignore("TODO - Intent is null in test so always passes, re-enable once fixed")
    @Test
    fun `onDoubleClick launches intent`() = runTest {
        presenter.test {
            val state = awaitItem()
            state.onDoubleClick()

            val expectedIntent = Intent(Intent.ACTION_VIEW, Uri.parse("sms:$testDestination"))
            verify(intentLauncher).startActivity(argThat { intent ->
                intent.action == expectedIntent.action && intent.data == expectedIntent.data
            })
        }
    }
}
