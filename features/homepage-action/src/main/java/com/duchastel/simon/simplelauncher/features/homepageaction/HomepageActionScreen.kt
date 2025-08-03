package com.duchastel.simon.simplelauncher.features.homepageaction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import dagger.hilt.components.SingletonComponent
import kotlinx.parcelize.Parcelize

@Parcelize
object HomepageActionScreen : Screen {
    data class State(val eventSink: (Event) -> Unit) : CircuitUiState
    sealed interface Event : CircuitUiEvent {
        object Kiss : Event
    }
}

@CircuitInject(HomepageActionScreen::class, SingletonComponent::class)
@Composable
fun HomepageAction(state: HomepageActionScreen.State, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "😘",
            fontSize = 100.sp,
            modifier = Modifier.clickable { state.eventSink(HomepageActionScreen.Event.Kiss) })
    }
}
