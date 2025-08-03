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
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.coroutines.Continuation

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
        smsBroadcastReceiverFactory = mock()
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
        val mockReceiver = mock<BroadcastReceiver>()
        whenever(smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(any(), any())) doAnswer { invocation ->
            val continuation = invocation.arguments[1] as Continuation<Boolean>
            // Simulate successful SMS delivery
            mockReceiver.apply { 
                doAnswer { 
                    resultCode = Activity.RESULT_OK
                    continuation.resumeWith(Result.success(true))
                }.whenever(this).onReceive(any(), any())
            }
            mockReceiver
        }

        val result = smsRepository.sendSms("1234567890", "test message")

        assertTrue(result)
        verify(smsManager).sendTextMessage(any(), any(), any(), any(), any())
        verify(context).registerReceiver(any(), any(), any<Int>())
        verify(context).unregisterReceiver(any())
    }

    @Test
    fun `sendSms sends message and returns false on failure`() = runTest {
        val mockReceiver = mock<BroadcastReceiver>()
        whenever(smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(any(), any())) doAnswer { invocation ->
            val continuation = invocation.arguments[1] as Continuation<Boolean>
            // Simulate failed SMS delivery
            mockReceiver.apply { 
                doAnswer { 
                    resultCode = Activity.RESULT_CANCELED
                    continuation.resumeWith(Result.success(false))
                }.whenever(this).onReceive(any(), any())
            }
            mockReceiver
        }

        val result = smsRepository.sendSms("1234567890", "test message")

        assertFalse(result)
        verify(smsManager).sendTextMessage(any(), any(), any(), any(), any())
        verify(context).registerReceiver(any(), any(), any<Int>())
        verify(context).unregisterReceiver(any())
    }
}