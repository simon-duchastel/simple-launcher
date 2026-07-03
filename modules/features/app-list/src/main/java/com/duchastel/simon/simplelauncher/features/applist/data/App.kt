package com.duchastel.simon.simplelauncher.features.applist.data

import android.graphics.drawable.Drawable

/**
 * Model representing an installed application that can be displayed and interacted with from the UI layer.
 *
 * @property label The user-visible display name of the application (e.g., "Maps").
 * @property packageName The unique package identifier for the app (e.g., "com.google.android.maps").
 * @property icon The drawable icon associated with the app for visual display.
 */
data class App(
    /**
     * The user-friendly display name of the app.
     */
    val label: String,

    /**
     * The app's package name, used as a unique identifier when launching apps or querying system info.
     */
    val packageName: String,

    /**
     * The drawable icon displayed in the launcher UI for this app.
     */
    val icon: Drawable
) {

    /**
     * Returns a normalized key used to sort apps alphabetically.
     *
     * Case is ignored so that e.g. "mTicket", "luckin coffee" and "Zoom" sort together by letter
     * instead of by ASCII code (where uppercase letters would otherwise come before lowercase ones).
     *
     * Leading non-letter characters are stripped so that labels like "*My App" sort under "M".
     * Labels that contain no letters at all (e.g. "*&^") are sorted after every lettered app,
     * keeping symbol-only apps grouped deterministically at the end.
     */
    fun sortKey(): String {
        val trimmed = label.trimStart()
        if (trimmed.isEmpty()) return SYMBOLS_AFTER_LETTERS + label.lowercase()
        val firstLetter = trimmed.indexOfFirst { it.isLetter() }
        return if (firstLetter == -1) {
            SYMBOLS_AFTER_LETTERS + label.lowercase()
        } else {
            LETTERS_FIRST + trimmed.substring(firstLetter).lowercase()
        }
    }

    private companion object {
        // Sort prefixes that guarantee lettered apps come before symbol-only apps, regardless of label content.
        private const val LETTERS_FIRST = '0'
        private const val SYMBOLS_AFTER_LETTERS = '1'
    }
}
