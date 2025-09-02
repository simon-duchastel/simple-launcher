package com.duchastel.simon.simplelauncher.features.appwidgets.di

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepositoryImpl
import com.duchastel.simon.simplelauncher.features.appwidgets.host.LauncherAppWidgetHost
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetPresenter
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetScreen
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetState
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetUi
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppWidgetsModule {

    @Binds
    abstract fun bindAppWidgetRepository(
        appWidgetRepositoryImpl: AppWidgetRepositoryImpl
    ): AppWidgetRepository

    companion object {
        
        @Provides
        @Singleton
        fun provideAppWidgetManager(
            @ApplicationContext context: Context
        ): AppWidgetManager = AppWidgetManager.getInstance(context)

        @Provides
        @IntoSet
        fun provideAppWidgetPresenterFactory(
            factory: AppWidgetPresenter.Factory
        ): Presenter.Factory = Presenter.Factory { screen, navigator, context ->
            when (screen) {
                is AppWidgetScreen -> factory.create(screen)
                else -> null
            }
        }

        @Provides
        @IntoSet
        fun provideAppWidgetUiFactory(
            appWidgetHost: LauncherAppWidgetHost
        ): Ui.Factory = Ui.Factory { screen, context ->
            when (screen) {
                is AppWidgetScreen -> {
                    object : Ui<AppWidgetState> {
                        @Composable
                        override fun Content(state: AppWidgetState, modifier: Modifier) {
                            AppWidgetUi(
                                state = state,
                                appWidgetHost = appWidgetHost,
                                modifier = modifier
                            )
                        }
                    }
                }
                else -> null
            }
        }
    }
}