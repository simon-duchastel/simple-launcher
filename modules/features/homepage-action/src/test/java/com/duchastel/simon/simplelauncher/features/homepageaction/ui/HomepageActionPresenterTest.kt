package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import android.content.Intent
import android.net.Uri
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.duchastel.simon.simplelauncher.libs.ui.extensions.LongClickScope
import com.slack.circuit.test.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doSuspendableAnswer
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class HomepageActionPresenterTest {
    private val smsRepository: SmsRepository = mock()
    private val intentLauncher: IntentLauncher = mock()
    private val testDestination = "+1234567890"
    private val testEmoji = "🥳"
    private val config = HomepageActionButton(
        smsDestination = testDestination,
        emoji = testEmoji,
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

    @Test
    fun `onLongClick calls sendSms on repository with repeated emoji`() = runTest {
        val scope = mock<LongClickScope> {
            onBlocking { tryAwaitRelease() } doAnswer {
                runBlocking {
                    delay(1500) // 3 emojis expected, since we delay 3x 500ms = 1500ms
                    true
                }
            }
        }
        whenever(smsRepository.sendSms(any(), any())).thenReturn(true)

        presenter.test {
            val state = awaitItem()

            state.onLongClick(scope)
            verify(smsRepository).sendSms(eq(testDestination), eq("🥳🥳🥳"))
        }
    }

    @Test
    fun `onLongClick does not sendSms on repository when cancelled`() = runTest {
        val scope = mock<LongClickScope> {
            onBlocking { tryAwaitRelease() }.thenReturn(false)
        }
        whenever(smsRepository.sendSms(any(), any())).thenReturn(true)

        presenter.test {
            val state = awaitItem()
            state.onLongClick(scope)
            verify(smsRepository, never()).sendSms(any(), any())
        }
    }
}
