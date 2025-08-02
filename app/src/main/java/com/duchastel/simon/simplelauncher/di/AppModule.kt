package com.duchastel.simon.simplelauncher.di

import android.content.Context
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepositoryImpl
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListPresenter
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.slack.circuit.foundation.Circuit
import com.duchastel.simon.simplelauncher.features.applist.ui.AppList
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListState
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.presenter.Presenter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.slack.circuit.runtime.ui.Ui
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    fun bindAppRepository(impl: AppRepositoryImpl): AppRepository

    companion object {
        @Provides
        @IntoSet
        fun provideAppListPresenterFactory(appRepository: AppRepository): Presenter.Factory {
            return Presenter.Factory { screen, navigator, circuitContext ->
                when (screen) {
                    is AppListScreen -> AppListPresenter(appRepository)
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

        @Provides
        fun provideCircuit(presenterFactories: Set<@JvmSuppressWildcards Presenter.Factory>, uiFactories: Set<@JvmSuppressWildcards Ui.Factory>): Circuit {
            return Circuit.Builder()
                .addPresenterFactories(presenterFactories)
                .addUiFactories(uiFactories)
                .build()
        }

        @Provides
        fun provideAppRepositoryImpl(@ApplicationContext context: Context): AppRepositoryImpl {
            return AppRepositoryImpl(context)
        }
    }
}