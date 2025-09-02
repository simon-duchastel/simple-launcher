package com.duchastel.simon.simplelauncher.libs.emoji.data

import javax.inject.Inject

class EmojiValidatorImpl @Inject constructor() : EmojiValidator {
    override fun isEmoji(text: String): Boolean {
        val asChar = text.firstOrNull() ?: return false
        return Character.getType(asChar) == Character.SURROGATE.toInt() ||
                Character.getType(asChar) == Character.OTHER_SYMBOL.toInt()
    }
}