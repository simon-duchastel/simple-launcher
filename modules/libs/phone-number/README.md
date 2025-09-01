# Phone Number Library

Contains phone number utilities.

## Purpose

This library module provides phone number validation functionality that can be easily mocked in unit tests. It abstracts Android's `Patterns.PHONE` regex to prevent `NullPointerException` issues in JVM-based unit tests.

## Key Components

- **PhoneNumberValidator** - Interface for phone number validation

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

## Supported Formats

The validator supports various phone number formats:
- US formats: `(555) 123-4567`, `555-123-4567`, `5551234567`
- International: `+1 555 123 4567`, `+44 20 7946 0958`
- With country codes: `+15551234567`