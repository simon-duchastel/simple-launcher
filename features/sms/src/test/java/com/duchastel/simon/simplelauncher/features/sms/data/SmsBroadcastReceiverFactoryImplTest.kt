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
import org.mockito.kotlin.never
import org.mockito.kotlin.verify

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
    fun `create returns a BroadcastReceiver with true on RESULT_OK`() = runTest {
        val messageId = "testMessageId"

        var result: Boolean? = null
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> result = successfullySent },
        )
        receiver.resultCode = Activity.RESULT_OK
        receiver.onReceive(
            context,
            Intent().apply { putExtra("messageId", messageId) },
        )

        assert(result == true)
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver with false on non-RESULT_OK`() = runTest {
        val messageId = "testMessageId"

        var result: Boolean? = null
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> result = successfullySent },
        )
        receiver.resultCode = Activity.RESULT_CANCELED
        receiver.onReceive(
            context,
            Intent().apply { putExtra("messageId", messageId) },
        )

        assert(result == false)
    }

    @Test
    fun `create returns a BroadcastReceiver that ignores intent with different messageId`() = runTest {
        val messageId = "testMessageId"
        val wrongMessageId = "wrongMessageId"

        var result: Boolean? = null
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> result = successfullySent },
        )
        receiver.onReceive(
            context,
            Intent().apply { putExtra("messageId", wrongMessageId) },
        )

        assert(result == null) // assert no result received
        verify(context, never()).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver that ignores intent without messageId`() = runTest {
        var result: Boolean? = null
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = "test-message-id",
            onSentSmsReceived = { successfullySent, _ -> result = successfullySent },
        )
        receiver.onReceive(context, Intent())

        assert(result == null) // assert no result received
    }
}
