package com.duchastel.simon.simplelauncher.features.applist.di

import android.content.Context
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AppListDataModule {
    @Binds
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

    companion object {
        @Provides
        fun provideAppRepositoryImpl(@ApplicationContext context: Context): AppRepositoryImpl {
            return AppRepositoryImpl(context)
        }
    }
}
