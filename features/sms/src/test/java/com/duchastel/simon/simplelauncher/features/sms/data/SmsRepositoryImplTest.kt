package com.duchastel.simon.simplelauncher.features.sms.data

import android.content.Context
import android.content.pm.PackageManager
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class SmsRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var permissionRepository: PermissionsRepository
    private lateinit var packageManager: PackageManager
    private lateinit var smsManager: SmsManager

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
        context = mock {
            on { packageManager } doReturn packageManager
            on { getSystemService(SmsManager::class.java) } doReturn smsManager
        }

        smsRepository = SmsRepositoryImpl(context, permissionRepository)
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
}
