package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.BindingState
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetDisplayState
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.AppWidgetHostCompose
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionButton
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object HomepageScreen : Screen, Parcelable

data class HomepageState(
    val text: String,
    val homepageAction: HomepageAction?,
    val widgetDisplayState: WidgetDisplayState?,
    val isLoadingWidget: Boolean,
) : CircuitUiState {
    data class HomepageAction(
        val emoji: String,
        val smsDestination: String,
    )
}

@Composable
internal fun Homepage(state: HomepageState) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoadingWidget -> {
                Text(
                    text = "Loading widget...",
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            
            state.widgetDisplayState?.boundWidget != null && 
            state.widgetDisplayState.bindingState == BindingState.Bound -> {
                val boundWidget = state.widgetDisplayState.boundWidget!!
                AppWidgetHostCompose(
                    widgetData = boundWidget,
                    widgetViewState = WidgetViewState.Loading, // Widget view will manage its own state
                    widgetHostView = null, // Widget host view will be created by the compose component
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            state.widgetDisplayState?.error != null -> {
                val error = state.widgetDisplayState.error
                val errorMessage = when (error) {
                    is WidgetError.ProviderNotFound -> "Selected widget is no longer available. The app may have been uninstalled."
                    is WidgetError.PermissionDenied -> "Widget permission denied. Please check your settings."
                    is WidgetError.HostCreationFailed -> "Failed to create widget view."
                    is WidgetError.WidgetBindingFailed -> "Failed to bind widget."
                    is WidgetError.Unknown -> "Widget error: ${error.message}"
                    null -> "Unknown error"
                }
                Text(
                    text = errorMessage,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            
            else -> {
                Text(
                    text = state.text,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
        
        if (state.homepageAction != null) {
            CircuitContent(
                HomepageActionButton(
                    smsDestination = state.homepageAction.smsDestination,
                    emoji = state.homepageAction.emoji,
                ),
                modifier = Modifier
                    .padding(
                        horizontal = 60.dp,
                        vertical = 100.dp,
                    )
                    .align(Alignment.TopEnd)
                    .rotate(15f)
            )
        }
    }
}

class HomepagePresenter @Inject internal constructor(
    private val settingsRepository: SettingsRepository,
    private val appWidgetRepository: AppWidgetRepository,
) : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        val homepageActionSettings by remember {
            settingsRepository.getSettingsFlow(Setting.HomepageAction) ?: flowOf(null)
        }.collectAsState(null)

        var widgetDisplayState by remember { mutableStateOf<WidgetDisplayState?>(null) }
        var isLoadingWidget by remember { mutableStateOf(false) }

        // Load widget display state whenever settings change
        val widgetSettings by remember {
            settingsRepository.getSettingsFlow(Setting.WidgetConfiguration) ?: flowOf(null)
        }.collectAsState(null)

        LaunchedEffect(widgetSettings) {
            isLoadingWidget = true
            try {
                val selectedWidget = (widgetSettings as? SettingData.WidgetConfigurationSettingData)?.widgetData
                widgetDisplayState = appWidgetRepository.getWidgetDisplayState(selectedWidget)
            } catch (e: Exception) {
                // Handle repository errors
                widgetDisplayState = null
            } finally {
                isLoadingWidget = false
            }
        }

        val homepageActionSettingsData = homepageActionSettings as? SettingData.HomepageActionSettingData
        
        return HomepageState(
            text = "Welcome back...",
            homepageAction = homepageActionSettingsData?.toUiType(),
            widgetDisplayState = widgetDisplayState,
            isLoadingWidget = isLoadingWidget,
        )
    }

    private fun SettingData.HomepageActionSettingData.toUiType() =
        HomepageState.HomepageAction(
            emoji = emoji,
            smsDestination = phoneNumber,
        )
}
