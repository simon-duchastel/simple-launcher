package com.duchastel.simon.simplelauncher.libs.phonenumber.di

import com.duchastel.simon.simplelauncher.libs.phonenumber.data.PhoneNumberValidator
import com.duchastel.simon.simplelauncher.libs.phonenumber.data.PhoneNumberValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PhoneNumberModule {

    @Binds
    abstract fun bindPhoneNumberValidator(impl: PhoneNumberValidatorImpl): PhoneNumberValidator
}