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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget.AppWidgetScreen
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionButton
import com.duchastel.simon.simplelauncher.features.settings.SettingsActivity
import com.duchastel.simon.simplelauncher.libs.ui.drawer.DrawerController
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object HomepageScreen : Screen, Parcelable

data class HomepageState(
    val homepageAction: HomepageAction?,
    val centerWidget: WidgetData?,
    val onSettingsClicked: () -> Unit,
    val onRequestDrawerClose: () -> Unit,
    val drawerCloseRequests: Flow<Unit>,
) : CircuitUiState {
    data class HomepageAction(
        val emoji: String,
        val smsDestination: String,
    )
}

@Composable
internal fun Homepage(state: HomepageState, modifier: Modifier = Modifier) {
    val drawerState = rememberVerticalSlidingDrawerState(DragAnchors.Hidden)
    val drawerScrollState = rememberLazyListState()
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

    LaunchedEffect(state.drawerCloseRequests, drawerState) {
        state.drawerCloseRequests.collect {
            drawerState.collapse()
        }
    }

    LaunchedEffect(drawerState, drawerScrollState) {
        resetAppListScrollOnDrawerHide(
            currentValueFlow = snapshotFlow { drawerState.currentValue },
            resetScroll = { drawerScrollState.scrollToItem(0) },
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        VerticalSlidingDrawer(
            state = drawerState,
            drawerContentScrollState = drawerScrollState,
            modifier = Modifier.fillMaxSize(),
            onDismissRequest = state.onRequestDrawerClose,
            drawerContent = {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircuitContent(AppListScreen)
                }
            },
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.centerWidget != null) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(
                                width = state.centerWidget.width.dp,
                                height = state.centerWidget.height.dp,
                            )
                    ) {
                        CircuitContent(AppWidgetScreen(state.centerWidget))
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

        SettingsButton(
            onClick = { state.onSettingsClicked() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .alpha(drawerState.progress),
        )
    }
}

/**
 * Resets the app list scroll position whenever the drawer moves to [DragAnchors.Hidden].
 */
internal suspend fun resetAppListScrollOnDrawerHide(
    currentValueFlow: Flow<DragAnchors>,
    resetScroll: suspend () -> Unit,
) {
    currentValueFlow.collect { currentValue ->
        if (currentValue == DragAnchors.Hidden) {
            resetScroll()
        }
    }
}

class HomepagePresenter @Inject internal constructor(
    @param:ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository,
    private val intentLauncher: IntentLauncher,
    private val drawerController: DrawerController,
) : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        val homepageSettings by remember {
            settingsRepository.getSettingsFlow(Setting.HomepageAction) ?: flowOf(null)
        }.collectAsState(null)

        val centerWidgetSettings by remember {
            settingsRepository.getSettingsFlow(Setting.CenterWidget) ?: flowOf(null)
        }.collectAsState(null)

        val homepageActionSettings = homepageSettings as? SettingData.HomepageActionSettingData
        val centerWidgetData = (centerWidgetSettings as? SettingData.CenterWidgetSettingData)?.widgetData
        return HomepageState(
            homepageAction = homepageActionSettings?.toUiType(),
            centerWidget = centerWidgetData,
            onSettingsClicked = {
                val intent = SettingsActivity.newActivityIntent(context)
                intentLauncher.startActivityAsSeparateApp(intent)
            },
            onRequestDrawerClose = { drawerController.requestClose() },
            drawerCloseRequests = drawerController.closeRequests,
        )
    }

    private fun SettingData.HomepageActionSettingData.toUiType() =
        HomepageState.HomepageAction(
            emoji = emoji,
            smsDestination = phoneNumber,
        )
}
