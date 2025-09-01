# Phone Number Library

Phone number validation utilities for the Simple Launcher application.

## Purpose

This library module provides phone number validation functionality that can be easily mocked in unit tests. It abstracts Android's `Patterns.PHONE` regex to prevent `NullPointerException` issues in JVM-based unit tests.

## Key Components

- **PhoneNumberValidator** - Interface for phone number validation
- **PhoneNumberValidatorImpl** - Implementation using Android's pattern matching
- **Dependency Injection** - Hilt module configuration

## Architecture

```
modules/libs/phone-number/
├── src/main/java/.../libs/phonenumber/
│   ├── data/
│   │   ├── PhoneNumberValidator.kt        # Validation interface
│   │   └── PhoneNumberValidatorImpl.kt    # Android patterns implementation
│   └── di/
│       └── PhoneNumberModule.kt           # Hilt DI configuration
└── src/test/                              # Unit tests with Robolectric
    └── java/.../phonenumber/data/
        └── PhoneNumberValidatorImplTest.kt
```

## Usage Examples

### Inject Validator
```kotlin
@Inject
lateinit var phoneNumberValidator: PhoneNumberValidator

// Validate phone number
val isValid = phoneNumberValidator.isValidPhoneNumber("555-123-4567")
```

### Testing with Mocks
```kotlin
@Mock
private lateinit var phoneNumberValidator: PhoneNumberValidator

@Test
fun testPhoneValidation() {
    whenever(phoneNumberValidator.isValidPhoneNumber("valid")).thenReturn(true)
    whenever(phoneNumberValidator.isValidPhoneNumber("invalid")).thenReturn(false)
    
    // Test your logic that uses the validator
}
```

## Supported Formats

The validator supports various phone number formats:
- US formats: `(555) 123-4567`, `555-123-4567`, `5551234567`
- International: `+1 555 123 4567`, `+44 20 7946 0958`
- With country codes: `+15551234567`

## Testing

- **Unit Tests** - Comprehensive validation testing with Robolectric
- **Mock Support** - Interface allows easy mocking for dependent components
- **Edge Cases** - Tests invalid inputs, empty strings, and malformed numbers

## Integration

This module integrates with:
- **Settings Feature** - Phone number validation in settings forms
- Any feature requiring phone number input validation

## Dependencies

- **Hilt** - Dependency injection
- **Robolectric** - Android unit testing framework
- **Android Patterns** - System regex patterns for validation