package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
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
        var widgetViewState by remember { mutableStateOf<WidgetViewState>(WidgetViewState.Loading) }
        var widgetHostView by remember { mutableStateOf<AppWidgetHostView?>(null) }

        LaunchedEffect(screen.widgetData.widgetId) {
            try {
                widgetViewState = WidgetViewState.Loading
                val hostView = appWidgetRepository.createWidgetView(screen.widgetData)
                widgetHostView = hostView
                widgetViewState = WidgetViewState.Success(screen.widgetData)
            } catch (e: SecurityException) {
                widgetViewState = WidgetViewState.Error(WidgetError.PermissionDenied)
            } catch (e: IllegalStateException) {
                widgetViewState = WidgetViewState.Error(WidgetError.ProviderNotFound)
            } catch (e: Exception) {
                widgetViewState = WidgetViewState.Error(WidgetError.Unknown(e.message ?: "Unknown error"))
            }
        }

        return AppWidgetState(
            widgetData = screen.widgetData,
            widgetViewState = widgetViewState,
            widgetHostView = widgetHostView,
            onRetry = {
                scope.launch {
                    try {
                        widgetViewState = WidgetViewState.Loading
                        val hostView = appWidgetRepository.createWidgetView(screen.widgetData)
                        widgetHostView = hostView
                        widgetViewState = WidgetViewState.Success(screen.widgetData)
                    } catch (e: SecurityException) {
                        widgetViewState = WidgetViewState.Error(WidgetError.PermissionDenied)
                    } catch (e: IllegalStateException) {
                        widgetViewState = WidgetViewState.Error(WidgetError.ProviderNotFound)
                    } catch (e: Exception) {
                        widgetViewState = WidgetViewState.Error(WidgetError.Unknown(e.message ?: "Unknown error"))
                    }
                }
            },
            onRemoveWidget = {
                scope.launch {
                    try {
                        appWidgetRepository.unbindWidget(screen.widgetData.widgetId)
                    } catch (e: Exception) {
                        // Handle removal errors if needed
                    }
                }
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: AppWidgetScreen): AppWidgetPresenter
    }
}