package com.duchastel.simon.simplelauncher.libs.phonenumber.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhoneNumberValidatorImplAndroidTest {

    private lateinit var validator: PhoneNumberValidator

    @Before
    fun setup() {
        validator = PhoneNumberValidatorImpl()
    }

    @Test
    fun shouldReturnTrueForValidPhoneNumbers() {
        // US format
        assert(validator.isValidPhoneNumber("(555) 123-4567"))
        assert(validator.isValidPhoneNumber("555-123-4567"))
        assert(validator.isValidPhoneNumber("5551234567"))
        assert(validator.isValidPhoneNumber("+1 555 123 4567"))
        assert(validator.isValidPhoneNumber("+15551234567"))
        
        // International format
        assert(validator.isValidPhoneNumber("+44 20 7946 0958"))
        assert(validator.isValidPhoneNumber("+33 1 42 68 53 00"))
    }

    @Test
    fun shouldReturnFalseForInvalidPhoneNumbers() {
        assert(!validator.isValidPhoneNumber(""))
        assert(!validator.isValidPhoneNumber("abc"))
        assert(!validator.isValidPhoneNumber("123"))
        assert(!validator.isValidPhoneNumber("NotAPhone"))
        assert(!validator.isValidPhoneNumber("12345"))
        assert(!validator.isValidPhoneNumber("abcd-efg-hijk"))
    }

    @Test
    fun shouldReturnFalseForEdgeCases() {
        assert(!validator.isValidPhoneNumber("   "))
        assert(!validator.isValidPhoneNumber("000-000-0000"))
        assert(!validator.isValidPhoneNumber("111-111-1111"))
    }
}