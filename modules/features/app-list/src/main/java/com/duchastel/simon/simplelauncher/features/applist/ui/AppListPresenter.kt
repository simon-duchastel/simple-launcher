package com.duchastel.simon.simplelauncher.features.applist.ui


import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.duchastel.simon.simplelauncher.features.applist.data.App
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.settings.SettingsActivity
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

/**
 * Screen for displaying the list of installed applications.
 */
@Parcelize
data object AppListScreen : Screen, Parcelable

data class AppListState(
    /**
     * The list of launchable apps available to show in the UI.
     */
    val apps: ImmutableList<App>,
    val onSettingsClicked: () -> Unit,
) : CircuitUiState {

    /**
     * UI model for representing a single app in the list.
     */
    data class App(
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

        /**
         * Lambda to launch the app's system settings when triggered from the UI.
         */
        val launchSystemSettings: () -> Unit,
    )
}

class AppListPresenter @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val appRepository: AppRepository,
    private val intentLauncher: IntentLauncher,
) : Presenter<AppListState> {

    @Composable
    override fun present(): AppListState {
        val apps = remember(appRepository) {
            appRepository.getInstalledApps()
                .map { app ->
                    app.toUiApp(
                        launchApp = { appRepository.launchApp(app) },
                        launchSystemSettings = { appRepository.launchAppSystemSettings(app) }
                    )
                }.toImmutableList()
        }

        return AppListState(
            apps = apps,
            onSettingsClicked = {
                val intent = SettingsActivity.newActivityIntent(context)
                intentLauncher.startActivityAsSeparateApp(intent)
            }
        )
    }
}

/**
 * Maps an [App] domain model to a UI [AppListState.App].
 */
private fun App.toUiApp(
    launchApp: () -> Unit,
    launchSystemSettings: () -> Unit,
): AppListState.App {
    return AppListState.App(
        label = label,
        icon = icon,
        launchApp = launchApp,
        launchSystemSettings = launchSystemSettings,
    )
}