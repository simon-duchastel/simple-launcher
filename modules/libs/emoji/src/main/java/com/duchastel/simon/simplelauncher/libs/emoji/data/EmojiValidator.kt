package com.duchastel.simon.simplelauncher.libs.emoji.data

/**
 * Interface for validating emoji characters.
 */
interface EmojiValidator {

    /**
     * Validates if a given string is a valid emoji.
     *
     * @param text The text string to validate (must contain only a single emoji).
     * @return true if the string is a single valid emoji, false if it is not a valid
     * emoji.
     */
    fun isEmoji(text: String): Boolean
}