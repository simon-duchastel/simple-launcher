package com.duchastel.simon.simplelauncher.features.applist.ui

import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.duchastel.simon.simplelauncher.features.applist.data.App
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.Parcelize

/**
 * Screen for displaying the list of installed applications.
 */
@Parcelize
class AppListScreen : Screen, Parcelable

internal data class AppListState(
    /**
     * The list of launchable apps available to show in the UI.
     */
    val apps: ImmutableList<App>,
) : CircuitUiState {

    /**
     * UI model for representing a single app in the list.
     */
    internal data class App(
        /**
         * The user-friendly name of the app (e.g., "Gmail").
         */
        val label: String,

        /**
         * The icon drawable representing the app.
         */
        val icon: Drawable,

        /**
         * Lambda to launch the app when triggered from the UI.
         */
        val launchApp: () -> Unit,
    )
}

internal class AppListPresenter(private val appRepository: AppRepository) : Presenter<AppListState> {

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
 * Maps an [App] domain model to a UI [AppListState.App].
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