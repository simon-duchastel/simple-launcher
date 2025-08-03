package com.duchastel.simon.simplelauncher.features.sms.di

import com.duchastel.simon.simplelauncher.features.sms.data.SmsRepository
import com.duchastel.simon.simplelauncher.features.sms.data.SmsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SmsModule {
    @Binds
    abstract fun bindSmsRepository(smsRepositoryImpl: SmsRepositoryImpl): SmsRepository
}
