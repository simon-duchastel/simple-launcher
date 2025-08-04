package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import android.content.Intent

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
            on { createSentSmsPendingIntent(any(), any(), any()) } doReturn mock()
        }
        context = mock {
            on { packageManager } doReturn packageManager
            on { getSystemService(SmsManager::class.java) } doReturn smsManager
        }

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
        val messageIdCaptor = ArgumentCaptor.forClass(String::class.java)
        val onSentSmsReceivedCaptor = ArgumentCaptor.forClass(Function2::class.java as Class<Function2<Boolean, BroadcastReceiver, Unit>>)
        lateinit var capturedReceiver: BroadcastReceiver

        whenever(smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(messageIdCaptor.capture(), onSentSmsReceivedCaptor.capture())) doAnswer {
            mock<BroadcastReceiver>().also { capturedReceiver = it }
        }

        val result = smsRepository.sendSms("1234567890", "test message")

        capturedReceiver.apply {
            // Simulate onReceive being called by the system
            onReceive(context, Intent())
            resultCode = Activity.RESULT_OK
            (onSentSmsReceivedCaptor.value as (Boolean, BroadcastReceiver) -> Unit).invoke(true, this)
        }

        assertTrue(result)
        verify(smsManager).sendTextMessage(any(), any(), any(), any(), any())
        verify(context).registerReceiver(eq(capturedReceiver), any(), any<Int>())
        verify(context).unregisterReceiver(eq(capturedReceiver))
    }

    @Test
    fun `sendSms sends message and returns false on failure`() = runTest {
        val messageIdCaptor = ArgumentCaptor.forClass(String::class.java)
        val onSentSmsReceivedCaptor = ArgumentCaptor.forClass(Function2::class.java as Class<Function2<Boolean, BroadcastReceiver, Unit>>)
        var capturedReceiver: BroadcastReceiver? = null

        whenever(smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(messageIdCaptor.capture(), onSentSmsReceivedCaptor.capture())) doAnswer {
            mock<BroadcastReceiver>().also { capturedReceiver = it }
        }

        val result = smsRepository.sendSms("1234567890", "test message")

        capturedReceiver?.apply {
            // Simulate onReceive being called by the system
            onReceive(context, Intent())
            resultCode = Activity.RESULT_CANCELED
            (onSentSmsReceivedCaptor.value as (Boolean, BroadcastReceiver) -> Unit).invoke(false, this)
        }

        assertFalse(result)
        verify(smsManager).sendTextMessage(any(), any(), any(), any(), any())
        verify(context).registerReceiver(eq(capturedReceiver), any(), any<Int>())
        verify(context).unregisterReceiver(eq(capturedReceiver))
    }
}