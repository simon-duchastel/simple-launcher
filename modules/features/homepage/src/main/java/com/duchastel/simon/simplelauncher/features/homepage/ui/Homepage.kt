package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.content.Context
import android.os.Parcelable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.libs.ui.components.rememberVerticalSlidingDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionButton
import com.duchastel.simon.simplelauncher.features.settings.SettingsActivity
import com.duchastel.simon.simplelauncher.features.settings.data.Setting
import com.duchastel.simon.simplelauncher.features.settings.data.SettingData
import com.duchastel.simon.simplelauncher.features.settings.data.SettingsRepository
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.duchastel.simon.simplelauncher.libs.ui.components.DragAnchors
import com.duchastel.simon.simplelauncher.libs.ui.components.SettingsButton
import com.duchastel.simon.simplelauncher.libs.ui.components.VerticalSlidingDrawer
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object HomepageScreen : Screen, Parcelable

data class HomepageState(
    val homepageAction: HomepageAction?,
    val onSettingsClicked: () -> Unit,
) : CircuitUiState {
    data class HomepageAction(
        val emoji: String,
        val smsDestination: String,
    )
}

@Composable
internal fun Homepage(state: HomepageState, modifier: Modifier = Modifier) {
    val drawerState = rememberVerticalSlidingDrawerState(DragAnchors.Hidden)
    val hapticFeedback by rememberUpdatedState(LocalHapticFeedback.current)

    LaunchedEffect(drawerState) {
        var previousTarget = drawerState.targetValue
        snapshotFlow { drawerState.targetValue }
            .collect { targetValue ->
                if (targetValue == DragAnchors.Expanded && previousTarget == DragAnchors.Hidden) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.GestureThresholdActivate)
                }
                previousTarget = targetValue
            }
    }

    Box(modifier = modifier.fillMaxSize()) {
        VerticalSlidingDrawer(
            state = drawerState,
            modifier = Modifier.fillMaxSize(),
            drawerContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircuitContent(AppListScreen)
                }
            },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
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

        SettingsButton(
            onClick = { state.onSettingsClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .alpha(drawerState.progress),
        )
    }
}

class HomepagePresenter @Inject internal constructor(
    @param:ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val intentLauncher: IntentLauncher,
) : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        val settings by remember {
            settingsRepository.getSettingsFlow(Setting.HomepageAction) ?: flowOf(null)
        }.collectAsState(null)

        val homepageActionSettings = settings as? SettingData.HomepageActionSettingData
        return HomepageState(
            homepageAction = homepageActionSettings?.toUiType(),
            onSettingsClicked = {
                val intent = SettingsActivity.newActivityIntent(context)
                intentLauncher.startActivityAsSeparateApp(intent)
            },
        )
    }

    private fun SettingData.HomepageActionSettingData.toUiType() =
        HomepageState.HomepageAction(
            emoji = emoji,
            smsDestination = phoneNumber,
        )
}
