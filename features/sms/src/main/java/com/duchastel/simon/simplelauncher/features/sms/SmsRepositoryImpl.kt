package com.duchastel.simon.simplelauncher.features.sms

import android.telephony.SmsManager

class SmsRepositoryImpl: SmsRepository {

    override fun sendSms(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }
}
