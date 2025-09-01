package com.duchastel.simon.simplelauncher.libs.emoji.data

/**
 * Interface for validating emoji characters.
 * 
 * Provides validation functionality for determining whether a given string
 * represents a valid emoji character. This interface allows for dependency
 * injection and easy mocking in unit tests.
 */
interface EmojiValidator {

    /**
     * Validates if a given string is a valid emoji.
     * 
     * Checks the first character of the input string to determine if it represents
     * a valid emoji. Recognizes both surrogate pairs (modern emojis like 😀, 🎉, 👍)
     * and symbol characters (traditional symbols like ♣️, ⭐, ©️).
     * 
     * @param text The text string to validate (only the first character is checked)
     * @return true if the first character is a valid emoji, false for empty strings,
     *         regular text, or non-emoji characters
     */
    fun isEmoji(text: String): Boolean
}