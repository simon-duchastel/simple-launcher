package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.libs.permissions.data.Permission
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsBroadcastReceiverFactory
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SmsRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var permissionRepository: PermissionsRepository
    private lateinit var packageManager: PackageManager
    private lateinit var smsManager: SmsManager
    private lateinit var smsBroadcastReceiverFactory: SmsBroadcastReceiverFactory

    private lateinit var smsRepository: SmsRepositoryImpl

    @Before
    fun setUp() {
        smsManager = mock()
        permissionRepository = mock {
            onBlocking { requestPermission(Permission.SEND_SMS) } doReturn true
        }
        packageManager = mock {
            on { hasSystemFeature(PackageManager.FEATURE_TELEPHONY) } doReturn true
        }
        smsBroadcastReceiverFactory = mock {
            on { createSentSmsBroadcastReceiver(any(), any()) } doReturn mock()
        }
        context = mock {
            on { packageManager } doReturn packageManager
            on { getSystemService(SmsManager::class.java) } doReturn smsManager
        }

        whenever(
            smsBroadcastReceiverFactory.createSentSmsPendingIntent(
                any(), any(), any()
            )
        ) doReturn mock()

        smsRepository = SmsRepositoryImpl(context, permissionRepository, smsBroadcastReceiverFactory)
    }

    @Test
    fun `sendSms returns false when permission denied`() = runTest {
        whenever(permissionRepository.requestPermission(Permission.SEND_SMS)).thenReturn(false)

        val result = smsRepository.sendSms("1234567890", "dummy message")
        assertFalse(result)
    }

    @Test
    fun `sendSms returns false when no telephony feature`() = runTest {
        whenever(packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY))
            .thenReturn(false)

        val result = smsRepository.sendSms("1234567890", "dummy message")
        assertFalse(result)
    }

    @Test
    fun `sendSms sends message and returns true on success`() = runTest {
        var onSentSmsReceived: ((Boolean, BroadcastReceiver) -> Unit)? = null
        val receiver: BroadcastReceiver = mock()

        whenever(
            smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(any(), any())
        ) doAnswer {
            onSentSmsReceived = it.getArgument(1)
            receiver
        }

        val asyncResult = async {
            smsRepository.sendSms("1234567890", "test message")
        }
        advanceUntilIdle()
        receiver.apply {
            onReceive(context, Intent())
            resultCode = Activity.RESULT_OK
            onSentSmsReceived?.invoke(true, this)
        }
        val result = asyncResult.await()

        assertTrue(result)
        verify(smsManager).sendTextMessage(any(), eq(null), any(), any(), anyOrNull())
        verify(context).registerReceiver(eq(receiver), any(), any<Int>())
        verify(context).unregisterReceiver(eq(receiver))
    }

    @Test
    fun `sendSms sends message and returns false on failure`() = runTest {
        var onSentSmsReceived: ((Boolean, BroadcastReceiver) -> Unit)? = null
        val receiver: BroadcastReceiver = mock()

        whenever(
            smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(any(), any())
        ) doAnswer {
            onSentSmsReceived = it.getArgument(1)
            receiver
        }

        val asyncResult = async {
            smsRepository.sendSms("1234567890", "test message")
        }
        advanceUntilIdle()
        receiver.apply {
            onReceive(context, Intent())
            resultCode = Activity.RESULT_CANCELED
            onSentSmsReceived?.invoke(false, this)
        }
        val result = asyncResult.await()

        assertFalse(result)
    }
}