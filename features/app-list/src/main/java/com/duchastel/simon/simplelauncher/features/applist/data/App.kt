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
)
