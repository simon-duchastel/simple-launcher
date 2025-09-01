package com.duchastel.simon.simplelauncher.libs.emoji.data

/**
 * Interface for validating emoji characters.
 */
interface EmojiValidator {

    /**
     * Validates if a given string is a valid emoji.
     * 
     * @param text The text string to validate
     * @return true if the string is a valid emoji, false otherwise
     */
    fun isEmoji(text: String): Boolean
}