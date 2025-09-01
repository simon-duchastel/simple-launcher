# Emoji Library

Contains emoji validation utilities.

## Purpose

This library module provides emoji validation functionality that can be easily mocked in unit tests. It abstracts emoji character type checking to enable testable and injectable validation logic for UI components.

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

## Supported Emoji Types

The validator recognizes emoji characters by checking:
- **Surrogate pairs** - Most modern emojis (😀, 🎉, 👍)
- **Symbol characters** - Traditional symbols (♣️, ⭐, ©️)

## Validation Rules

- Returns `true` for valid single emoji characters
- Returns `false` for empty strings, regular text, or multi-character strings
- Only validates the first character of the input string