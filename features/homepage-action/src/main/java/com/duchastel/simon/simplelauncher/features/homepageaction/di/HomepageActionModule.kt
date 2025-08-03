package com.duchastel.simon.simplelauncher.features.homepageaction.di

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageAction
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionPresenter
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionScreen
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
object HomepageActionModule {
    @Provides
    @IntoSet
    fun provideAppListPresenterFactory(
        homepageActionPresenter: HomepageActionPresenter,
    ): Presenter.Factory {
        return Presenter.Factory { screen, navigator, circuitContext ->
            when (screen) {
                is HomepageActionScreen -> homepageActionPresenter
                else -> null
            }
        }
    }

    @Provides
    @IntoSet
    fun provideAppListUiFactory(): Ui.Factory {
        return Ui.Factory { screen, _ ->
            when (screen) {
                is HomepageActionScreen -> {
                    object : Ui<HomepageActionState> {
                        @Composable
                        override fun Content(state: HomepageActionState, modifier: Modifier) {
                            HomepageAction(state, modifier)
                        }
                    }
                }

                else -> null
            }
        }
    }
}
