package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
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
    val widgetData: WidgetData?,
) : CircuitUiState {
    data class HomepageAction(
        val emoji: String,
        val smsDestination: String,
    )
}

@Composable
internal fun Homepage(state: HomepageState) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (state.widgetData != null) {
            AppWidgetHostCompose(
                widgetData = state.widgetData,
                modifier = Modifier.align(Alignment.Center),
                onError = { /* TODO: Handle widget error */ }
            )
        } else {
            Text(
                text = state.text,
                modifier = Modifier.align(Alignment.Center),
            )
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
) : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        val homepageActionSettings by remember {
            settingsRepository.getSettingsFlow(Setting.HomepageAction) ?: flowOf(null)
        }.collectAsState(null)

        val widgetSettings by remember {
            settingsRepository.getSettingsFlow(Setting.WidgetConfiguration) ?: flowOf(null)
        }.collectAsState(null)

        val homepageActionSettingsData = homepageActionSettings as? SettingData.HomepageActionSettingData
        val widgetConfigurationData = widgetSettings as? SettingData.WidgetConfigurationSettingData
        
        return HomepageState(
            text = "Welcome back...",
            homepageAction = homepageActionSettingsData?.toUiType(),
            widgetData = widgetConfigurationData?.widgetData,
        )
    }

    private fun SettingData.HomepageActionSettingData.toUiType() =
        HomepageState.HomepageAction(
            emoji = emoji,
            smsDestination = phoneNumber,
        )
}
