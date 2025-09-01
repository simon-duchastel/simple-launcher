package com.duchastel.simon.simplelauncher.libs.contacts.di

import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class ContactsModule {

    @Binds
    abstract fun bindContactsRepository(impl: ContactsRepositoryImpl): ContactsRepository
}