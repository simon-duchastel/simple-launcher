package com.duchastel.simon.simplelauncher.features.applist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.duchastel.simon.simplelauncher.features.applist.data.AppInfo
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepository
import com.duchastel.simon.simplelauncher.features.applist.data.AppRepositoryImpl
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class AppListScreen : Screen, Parcelable

data class AppListState(
    val apps: ImmutableList<AppInfo>,
    val eventSink: (AppListEvent) -> Unit
) : CircuitUiState

sealed interface AppListEvent : CircuitUiEvent {
    data class LaunchApp(val packageName: String) : AppListEvent
}

class AppListPresenter(private val appRepository: AppRepository) : Presenter<AppListState> {

    @Composable
    override fun present(): AppListState {
        val apps = remember { appRepository.getInstalledApps().toImmutableList() }

        return AppListState(apps) {
            when (it) {
                is AppListEvent.LaunchApp -> {
                    appRepository.launchApp(it.packageName)?.let { intent ->
                        // This should ideally be handled by a side effect or a navigation event
                        // For now, we'll use LocalContext directly.
                        // In a real Circuit app, you'd likely have a way to trigger Android-specific actions
                        // outside of the Presenter, perhaps via a custom Navigator or a dedicated effect handler.
                        // For this example, we'll pass the context from the composable.
                    }
                }
            }
        }
    }
}
