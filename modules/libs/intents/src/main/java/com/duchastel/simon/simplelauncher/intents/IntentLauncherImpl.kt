package com.duchastel.simon.simplelauncher.intents

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IntentLauncherImpl @Inject constructor(
    @ActivityContext private val context: Context,
) : IntentLauncher {

    override fun startActivity(intent: Intent) {
        context.startActivity(intent)
    }

    override fun startActivityAsSeparateApp(intent: Intent) {
        context.startActivity(intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
