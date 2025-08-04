package com.duchastel.simon.simplelauncher.features.sms.di

import com.duchastel.simon.simplelauncher.features.sms.data.SmsBroadcastReceiverFactory
import com.duchastel.simon.simplelauncher.features.sms.data.SmsBroadcastReceiverFactoryImpl
import com.duchastel.simon.simplelauncher.features.sms.data.SmsRepository
import com.duchastel.simon.simplelauncher.features.sms.data.SmsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class SmsModule {

    @Binds
    abstract fun bindSmsBroadcastReceiverFactory(
        smsBroadcastReceiverFactoryImpl: SmsBroadcastReceiverFactoryImpl
    ): SmsBroadcastReceiverFactory

    @Binds
    abstract fun bindSmsRepository(
        smsRepositoryImpl: SmsRepositoryImpl
    ): SmsRepository
}
