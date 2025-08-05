package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionButton
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
data object HomepageScreen : Screen, Parcelable

data class HomepageState(
    val text: String,
) : CircuitUiState

@Composable
internal fun Homepage(state: HomepageState) {
    Box(modifier = Modifier.fillMaxSize()) {
        CircuitContent(
            HomepageActionButton,
            modifier = Modifier
                .padding(
                    horizontal = 60.dp,
                    vertical = 100.dp,
                )
                .align(Alignment.TopEnd)
                .rotate(15f)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = state.text)
        }
    }
}

class HomepagePresenter @Inject internal constructor() : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        return HomepageState(text = "Welcome back...")
    }
}
