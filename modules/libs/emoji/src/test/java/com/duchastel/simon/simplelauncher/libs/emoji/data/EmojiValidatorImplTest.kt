package com.duchastel.simon.simplelauncher.libs.emoji.data

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class EmojiValidatorImplTest {

    private lateinit var validator: EmojiValidator

    @Before
    fun setup() {
        validator = EmojiValidatorImpl()
    }

    @Test
    fun shouldReturnTrueForValidEmojis() {
        // Common emojis
        assert(validator.isEmoji("😀"))
        assert(validator.isEmoji("❤️"))
        assert(validator.isEmoji("🎉"))
        assert(validator.isEmoji("👍"))
        assert(validator.isEmoji("🔥"))
        
        // Other symbols that should be valid
        assert(validator.isEmoji("♣️"))
        assert(validator.isEmoji("⭐"))
        assert(validator.isEmoji("©️"))
    }

    @Test
    fun shouldReturnFalseForNonEmojiCharacters() {
        // Regular text
        assert(!validator.isEmoji("a"))
        assert(!validator.isEmoji("1"))
        assert(!validator.isEmoji("Hello"))
        assert(!validator.isEmoji("123"))
        assert(!validator.isEmoji("abc"))
        
        // Punctuation and special characters
        assert(!validator.isEmoji("!"))
        assert(!validator.isEmoji("@"))
        assert(!validator.isEmoji("#"))
        assert(!validator.isEmoji("$"))
    }

    @Test
    fun shouldReturnFalseForMultipleCharacters() {
        // Multi-character strings starting with non-emoji
        assert(!validator.isEmoji("a😀"))
        assert(!validator.isEmoji("1❤️"))
    }

    @Test
    fun shouldReturnFalseForWhitespaceAndEmptyStrings() {
        // Empty string
        assert(!validator.isEmoji(""))

        // Whitespace
        assert(!validator.isEmoji(" "))
        assert(!validator.isEmoji("   "))
    }
}