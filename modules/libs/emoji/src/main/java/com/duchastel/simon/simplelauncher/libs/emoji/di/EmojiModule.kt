package com.duchastel.simon.simplelauncher.libs.emoji.di

import com.duchastel.simon.simplelauncher.libs.emoji.data.EmojiValidator
import com.duchastel.simon.simplelauncher.libs.emoji.data.EmojiValidatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class EmojiModule {

    @Binds
    abstract fun bindEmojiValidator(impl: EmojiValidatorImpl): EmojiValidator
}