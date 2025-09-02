package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class AppWidgetPresenter @AssistedInject internal constructor(
    @Assisted private val screen: AppWidgetScreen,
    private val appWidgetRepository: AppWidgetRepository
) : Presenter<AppWidgetState> {

    @Composable
    override fun present(): AppWidgetState {
        var isLoading by remember { mutableStateOf(false) }
        var error by remember { mutableStateOf<String?>(null) }

        return AppWidgetState(
            widgetData = screen.widgetData,
            isLoading = isLoading,
            error = error,
            onRetry = {
                error = null
                // Could trigger a widget refresh here if needed
            },
            onRemoveWidget = {
                isLoading = true
                // Remove the widget (this would typically trigger navigation back)
                // Note: Implementation would depend on how removal is handled in the app
            },
            onWidgetError = { throwable ->
                error = throwable.message ?: "Unknown widget error"
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(screen: AppWidgetScreen): AppWidgetPresenter
    }
}