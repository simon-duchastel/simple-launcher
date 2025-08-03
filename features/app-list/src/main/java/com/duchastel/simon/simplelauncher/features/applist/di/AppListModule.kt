package com.duchastel.simon.simplelauncher.features.applist.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepositoryImpl
import com.duchastel.simon.simplelauncher.features.applist.ui.AppList
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListPresenter
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet


@Module
@InstallIn(SingletonComponent::class)
abstract class AppListModule {
    @Binds
    abstract fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

    companion object {
        @Provides
        @IntoSet
        fun provideAppListPresenterFactory(appPresenter: AppListPresenter): Presenter.Factory {
            return Presenter.Factory { screen, navigator, circuitContext ->
                when (screen) {
                    is AppListScreen -> appPresenter
                    else -> null
                }
            }
        }

        @Provides
        @IntoSet
        fun provideAppListUiFactory(): Ui.Factory {
            return Ui.Factory { screen, _ ->
                when (screen) {
                    is AppListScreen -> {
                        object : Ui<AppListState> {
                            @Composable
                            override fun Content(state: AppListState, modifier: Modifier) {
                                AppList(state)
                            }
                        }
                    }

                    else -> null
                }
            }
        }
    }
}
