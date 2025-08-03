package com.duchastel.simon.simplelauncher.features.applist.ui

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

import android.os.Parcelable
import com.duchastel.simon.simplelauncher.features.applist.data.App
import kotlinx.parcelize.Parcelize

@Parcelize
class AppListScreen : Screen, Parcelable

data class AppListState(
    /**
     *
     */
    val apps: ImmutableList<App>,
) : CircuitUiState {

    /**
     *
     */
    data class App(
        /**
         *
         */
        val label: String,

        /**
         *
         */
        val icon: Drawable,

        /**
         *
         */
        val launchApp: () -> Unit,
    )
}

class AppListPresenter(private val appRepository: AppRepository) : Presenter<AppListState> {

    @Composable
    override fun present(): AppListState {
        val apps = remember(appRepository) {
            appRepository.getInstalledApps()
                .map { app ->
                    app.toUiApp(launchApp = { appRepository.launchApp(app) })
                }.toImmutableList()
        }

        return AppListState(apps = apps)
    }
}

/**
 *
 */
private fun App.toUiApp(
    launchApp: () -> Unit,
): AppListState.App {
    return AppListState.App(
        label = label,
        icon = icon,
        launchApp = launchApp,
    )
}