package com.duchastel.simon.simplelauncher.features.sms.data

import android.content.Context
import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SmsRepositoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context
): SmsRepository {

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)

    override suspend fun sendSms(phoneNumber: String, message: String) {
        smsManager.sendTextMessage(
            phoneNumber, // destinationAddress
            null,        // scAddress
            message,     // text
            null,        // sentIntent
            null,        // deliveryIntent
        )

        // TODO - wait for successful sent intent via activity result callback (wrap in suspend fun)
    }
}
