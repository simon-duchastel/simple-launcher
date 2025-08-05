package com.duchastel.simon.simplelauncher.settings.di

import com.duchastel.simon.simplelauncher.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.settings.data.SettingsRepositoryImpl
import com.duchastel.simon.simplelauncher.settings.ui.Settings
import com.duchastel.simon.simplelauncher.settings.ui.SettingsPresenter
import com.duchastel.simon.simplelauncher.settings.ui.SettingsScreen
import com.duchastel.simon.simplelauncher.settings.ui.SettingsState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @IntoSet
        fun provideSettingsUiFactory(): Ui.Factory = object : Ui.Factory {
            override fun create(screen: Screen, context: CircuitContext): Ui<*>? {
                return when (screen) {
                    is SettingsScreen -> ui<SettingsState> { state, modifier ->
                        Settings(state, modifier)
                    }
                    else -> null
                }
            }
        }

        @Provides
        @IntoSet
        fun provideSettingsPresenterFactory(
            settingsPresenterFactory: SettingsPresenter.Factory,
        ): Presenter.Factory {
            return Presenter.Factory { screen, navigator, _ ->
                when (screen) {
                    is SettingsScreen -> settingsPresenterFactory.create(navigator = navigator)
                    else -> null
                }
            }
        }
    }
}