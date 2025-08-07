package com.duchastel.simon.simplelauncher.intents

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class IntentsModule {
    @Binds
    abstract fun bindIntentLauncher(impl: IntentLauncherImpl): IntentLauncher
}
