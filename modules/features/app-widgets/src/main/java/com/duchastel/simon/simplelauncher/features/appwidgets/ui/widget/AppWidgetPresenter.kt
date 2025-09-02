package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class AppWidgetPresenter @AssistedInject internal constructor(
    @Assisted private val screen: AppWidgetScreen,
    private val appWidgetRepository: AppWidgetRepository
) : Presenter<AppWidgetState> {

    @Composable
    override fun present(): AppWidgetState {
        val scope = rememberCoroutineScope()
        val widgetViewState by appWidgetRepository.getWidgetViewState(screen.widgetData.widgetId).collectAsState(initial = WidgetViewState.Loading)
        var widgetHostView by remember { mutableStateOf<AppWidgetHostView?>(null) }

        LaunchedEffect(screen.widgetData.widgetId) {
            // Create widget view through repository
            appWidgetRepository.createWidgetView(screen.widgetData).fold(
                onSuccess = { hostView ->
                    widgetHostView = hostView
                },
                onFailure = {
                    // Error is already handled in repository through widget view state
                }
            )
        }

        return AppWidgetState(
            widgetData = screen.widgetData,
            widgetViewState = widgetViewState,
            widgetHostView = widgetHostView,
            onRetry = {
                scope.launch {
                    widgetHostView = null
                    appWidgetRepository.createWidgetView(screen.widgetData).fold(
                        onSuccess = { hostView ->
                            widgetHostView = hostView
                        },
                        onFailure = {
                            // Error is already handled in repository
                        }
                    )
                }
            },
            onRemoveWidget = {
                scope.launch {
                    appWidgetRepository.removeWidget(screen.widgetData.widgetId)
                    // Note: Navigation back would be handled by the calling component
                }
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: AppWidgetScreen): AppWidgetPresenter
    }
}