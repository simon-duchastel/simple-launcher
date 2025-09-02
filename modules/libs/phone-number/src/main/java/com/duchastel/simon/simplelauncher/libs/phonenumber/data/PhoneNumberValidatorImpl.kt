package com.duchastel.simon.simplelauncher.libs.phonenumber.data

import android.util.Patterns
import javax.inject.Inject

class PhoneNumberValidatorImpl @Inject constructor() : PhoneNumberValidator {
    override fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return Patterns.PHONE.matcher(phoneNumber).matches()
    }
}