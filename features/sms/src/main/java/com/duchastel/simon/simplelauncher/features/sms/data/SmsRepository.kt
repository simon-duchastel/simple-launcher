package com.duchastel.simon.simplelauncher.features.sms.data

/**
 *
 */
interface SmsRepository {

    /**
     *
     */
    suspend fun sendSms(phoneNumber: String, message: String)
}
