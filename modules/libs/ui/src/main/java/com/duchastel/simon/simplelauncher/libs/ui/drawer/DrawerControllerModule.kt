package com.duchastel.simon.simplelauncher.libs.ui.drawer

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DrawerControllerModule {
    @Binds
    abstract fun bindDrawerController(impl: DrawerControllerImpl): DrawerController
}
