package com.duchastel.simon.simplelauncher.features.applist.di

import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppListModule {
    @Binds
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository
}
