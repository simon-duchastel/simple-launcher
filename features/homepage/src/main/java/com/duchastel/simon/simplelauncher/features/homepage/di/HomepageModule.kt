package com.duchastel.simon.simplelauncher.features.homepage.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.homepage.ui.Homepage
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepagePresenter
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageScreen
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
class HomepageModule {
    @Provides
    @IntoSet
    fun provideHomepagePresenterFactory(homepagePresenter: HomepagePresenter): Presenter.Factory {
        return Presenter.Factory { screen, _, _ ->
            when (screen) {
                is HomepageScreen -> homepagePresenter
                else -> null
            }
        }
    }

    @Provides
    @IntoSet
    fun provideHomepageUiFactory(): Ui.Factory {
        return Ui.Factory { screen, _ ->
            when (screen) {
                is HomepageScreen -> {
                    object : Ui<HomepageState> {
                        @Composable
                        override fun Content(state: HomepageState, modifier: Modifier) {
                            Homepage(state)
                        }
                    }
                }

                else -> null
            }
        }
    }
}
