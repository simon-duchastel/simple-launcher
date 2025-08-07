package com.duchastel.simon.simplelauncher.intents

import android.app.Activity
import android.content.Intent

/**
 * Interface for launching Android [Intent]s.
 *
 * Note that this must be used from within an [Activity] or a dependency
 * that has access to an activity since only activities can start other
 * activities.
 */
interface IntentLauncher {

    /**
     * Starts the [Activity] for the given [intent].
     */
    fun startActivity(intent: Intent)

    /**
     * Starts the [Activity] for the given [intent] as a new task with a
     * different task affinity than the main launcher. From the user's
     * perspective this will appear as if the opened activity is a new app.
     */
    fun startActivityAsSeparateApp(intent: Intent)
}
