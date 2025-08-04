package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.os.Parcelable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@Parcelize
object HomepageScreen : Screen, Parcelable

data class HomepageState(
    val text: String = "Hello, World!"
) : CircuitUiState

@Composable
internal fun Homepage(state: HomepageState) {
    Text(text = state.text)
}

class HomepagePresenter @Inject internal constructor() : Presenter<HomepageState> {
    @Composable
    override fun present(): HomepageState {
        return HomepageState()
    }
}
