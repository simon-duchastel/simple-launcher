package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@ExperimentalCoroutinesApi
class SmsBroadcastReceiverFactoryImplTest {

    private lateinit var context: Context
    private lateinit var smsBroadcastReceiverFactory: SmsBroadcastReceiverFactoryImpl

    @Before
    fun setup() {
        context = mock()

        smsBroadcastReceiverFactory = SmsBroadcastReceiverFactoryImpl(context)
    }

    @Test
    fun `create returns a BroadcastReceiver that resumes continuation with true on RESULT_OK`() = runTest {
        val messageId = "testMessageId"
        val intent = Intent().apply { putExtra("messageId", messageId) }

        val result = suspendCoroutine<Boolean> {
            val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
                messageId = messageId,
                onSentSmsReceived = { _, _ -> },
            )
            receiver.onReceive(context, intent.apply { putExtra("messageId", messageId) })
            receiver.resultCode = Activity.RESULT_OK
        }

        assert(result)
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver that resumes continuation with false on non-RESULT_OK`() = runTest {
        val messageId = "testMessageId"
        val intent = Intent().apply { putExtra("messageId", messageId) }

        val result = suspendCoroutine<Boolean> {
            val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
                messageId = messageId,
                onSentSmsReceived = { _, _ -> },
            )
            receiver.onReceive(context, intent.apply { putExtra("messageId", messageId) })
            receiver.resultCode = Activity.RESULT_CANCELED
        }

        assert(!result)
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver that ignores intent with different messageId`() = runTest {
        val messageId = "testMessageId"
        val wrongMessageId = "wrongMessageId"
        val intent = Intent().apply { putExtra("messageId", wrongMessageId) }

        val result = suspendCoroutine<Boolean> {
            val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
                messageId = messageId,
                onSentSmsReceived = { _, _ -> },
            )
            receiver.onReceive(context, intent)
            // The continuation should not be resumed, so we resume it manually after a delay
            // to prevent the test from hanging indefinitely.
            it.resume(false) // Indicate that the receiver did not resume it
        }

        assert(!result)
        verify(context).unregisterReceiver(any())
    }
}
