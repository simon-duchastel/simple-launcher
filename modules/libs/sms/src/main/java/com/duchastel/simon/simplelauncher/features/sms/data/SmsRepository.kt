package com.duchastel.simon.simplelauncher.features.sms.data

/**
 * Repository for sending, receiving, and managing SMS messages.
 */
interface SmsRepository {

    /**
     * Sends an SMS message to the specified phone number.
     *
     * @param phoneNumber The recipient's phone number in international or local format.
     * @param message The content of the SMS to be sent.
     * @return `true` if the message was sent successfully, `false` otherwise.
     */
    suspend fun sendSms(phoneNumber: String, message: String): Boolean
}
