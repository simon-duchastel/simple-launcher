package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.never

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

        val deferred = CompletableDeferred<Boolean>()
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> deferred.complete(successfullySent) },
        )
        receiver.resultCode = Activity.RESULT_OK
        receiver.onReceive(context, intent.apply { putExtra(SmsBroadcastReceiverFactoryImpl.EXTRA_MESSAGE_ID, messageId) })
        val result = deferred.await()

        assert(result)
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver that resumes continuation with false on non-RESULT_OK`() = runTest {
        val messageId = "testMessageId"
        val intent = Intent().apply { putExtra("messageId", messageId) }

        val deferred = CompletableDeferred<Boolean>()
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> deferred.complete(successfullySent) },
        )
        receiver.resultCode = Activity.RESULT_CANCELED
        receiver.onReceive(context, intent.apply { putExtra(SmsBroadcastReceiverFactoryImpl.EXTRA_MESSAGE_ID, messageId) })
        val result = deferred.await()

        assert(!result)
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `create returns a BroadcastReceiver that ignores intent with different messageId`() = runTest {
        val messageId = "testMessageId"
        val wrongMessageId = "wrongMessageId"
        val intent = Intent().apply { putExtra("messageId", wrongMessageId) }

        val deferred = CompletableDeferred<Boolean>()
        val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
            messageId = messageId,
            onSentSmsReceived = { successfullySent, _ -> deferred.complete(successfullySent) },
        )
        receiver.onReceive(context, intent.apply { putExtra(SmsBroadcastReceiverFactoryImpl.EXTRA_MESSAGE_ID, wrongMessageId) })
        // The onSentSmsReceived should not be called, so the deferred should not be completed.
        // We'll just let the test timeout if it does.
        assert(!deferred.isCompleted)
        verify(context, never()).unregisterReceiver(any())
    }
}
