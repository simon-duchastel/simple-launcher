package com.duchastel.simon.simplelauncher.features.sms

/**
 *
 */
interface SmsRepository {

    /**
     *
     */
    fun sendSms(phoneNumber: String, message: String)
}
