package com.duchastel.simon.simplelauncher.features.settings.di

import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepositoryImpl
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingPresenter
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingScreen
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState

import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsPresenter
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsScreen
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingContent
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
abstract class SettingsModule {
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @IntoSet
        fun provideSettingsUiFactory(): Ui.Factory = Ui.Factory { screen, context ->
            when (screen) {
                is SettingsScreen -> ui<SettingsState> { state, modifier ->
                    SettingsScreen(state, modifier)
                }
                is ModifySettingScreen -> ui<ModifySettingState>  { state, modifier ->
                    ModifySettingContent(state, modifier)
                }
                else -> null
            }
        }

        @Provides
        @IntoSet
        fun provideSettingsPresenterFactory(
            settingsPresenterFactory: SettingsPresenter.Factory,
            modifySettingPresenterFactory: ModifySettingPresenter.Factory,
        ): Presenter.Factory {
            return Presenter.Factory { screen, navigator, context ->
                when (screen) {
                    is SettingsScreen -> settingsPresenterFactory.create(navigator)
                    is ModifySettingScreen -> modifySettingPresenterFactory.create(screen, navigator)
                    else -> null
                }
            }
        }
    }
}