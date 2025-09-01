package com.duchastel.simon.simplelauncher.libs.phonenumber.data

/**
 * Interface for validating phone numbers.
 */
interface PhoneNumberValidator {

    /**
     * Validates if a given phone number string is in a valid format.
     * 
     * @param phoneNumber The phone number string to validate
     * @return true if the phone number is valid, false otherwise
     */
    fun isValidPhoneNumber(phoneNumber: String): Boolean
}