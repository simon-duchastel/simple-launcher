# Emoji Library

Contains emoji utilities.

## Purpose

This library module provides emoji functionality, including validation.

## Key Components

- **EmojiValidator** - Interface for emoji validation

## Architecture

```
modules/libs/emoji/
├── src/main/java/.../libs/emoji/
│   ├── data/
│   │   ├── EmojiValidator.kt        # Validation interface
│   │   └── EmojiValidatorImpl.kt    # Character type implementation
│   └── di/
│       └── EmojiModule.kt           # Hilt DI configuration
└── src/test/                        # Unit tests with Robolectric
    └── java/.../emoji/data/
        └── EmojiValidatorImplTest.kt
```

## Usage Examples

### Inject Validator
```kotlin
@Inject
lateinit var emojiValidator: EmojiValidator

// Validate emoji
val isValid = emojiValidator.isEmoji("😀")
```
