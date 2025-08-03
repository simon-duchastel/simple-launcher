package com.duchastel.simon.simplelauncher.features.sms.data

import android.content.Context
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SmsRepositoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val permissionRepository: PermissionsRepository,
): SmsRepository {

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    override suspend fun sendSms(phoneNumber: String, message: String): Boolean {
        val result: Boolean = permissionRepository.requestPermission(Permission.SEND_SMS)

//        smsManager.sendTextMessage(
//            phoneNumber, // destinationAddress
//            null,        // scAddress
//            message,     // text
//            null,        // sentIntent
//            null,        // deliveryIntent
//        )

        return result
    }
}
