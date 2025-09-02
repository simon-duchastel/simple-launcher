package com.duchastel.simon.simplelauncher.libs.phonenumber.data

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class PhoneNumberValidatorImplTest {

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
    fun shouldReturnFalseForNonDigitsInPhoneNumbers() {
        // These should definitely be invalid according to Android Patterns.PHONE
        val invalidCases = listOf("", "abc", "NotAPhone", "abcd-efg-hijk")
        invalidCases.forEach { testCase ->
            assert(!validator.isValidPhoneNumber(testCase)) { "Expected '$testCase' to be invalid but got valid" }
        }
    }

    @Test
    fun shouldReturnTrueForOnlyDigitsValidNumbers() {
        // These are considered valid by Android's Patterns.PHONE (even if they seem invalid to humans)
        assert(validator.isValidPhoneNumber("123"))
        assert(validator.isValidPhoneNumber("12345"))
        assert(validator.isValidPhoneNumber("000-000-0000"))
        assert(validator.isValidPhoneNumber("111-111-1111"))
    }

    @Test
    fun shouldReturnFalseForEdgeCases() {
        // Only spaces should be invalid
        assert(!validator.isValidPhoneNumber("   "))
    }
}