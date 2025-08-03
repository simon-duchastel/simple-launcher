package com.duchastel.simon.simplelauncher.features.sms.data

import android.content.Context
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class SmsRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var permissionRepository: PermissionsRepository
    private lateinit var smsRepository: SmsRepositoryImpl

    @Before
    fun setUp() {
        context = mock()
        permissionRepository = mock()
        whenever(context.getSystemService(SmsManager::class.java)).thenReturn(mock())
        smsRepository = SmsRepositoryImpl(context, permissionRepository)
    }

    @Test
    fun `sendSms returns false when permission denied`() = runTest {
        whenever(permissionRepository.requestPermission(Permission.SEND_SMS)).thenReturn(false)
        val result = smsRepository.sendSms("1234567890", "dummy message")
        assertFalse(result)
    }
}
