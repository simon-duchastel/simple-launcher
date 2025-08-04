package com.duchastel.simon.simplelauncher.libs.permissions.di

import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class PermissionsModule {

    @Binds
    abstract fun bindPermissionsRepository(impl: PermissionsRepositoryImpl): PermissionsRepository
}
