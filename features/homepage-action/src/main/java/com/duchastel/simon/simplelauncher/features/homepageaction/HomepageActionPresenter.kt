package com.duchastel.simon.simplelauncher.features.homepageaction

import androidx.compose.runtime.Composable
import com.duchastel.simon.simplelauncher.features.sms.SmsRepository
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
object HomepageActionScreen : Screen

data class HomepageActionState(
    val onClick: () -> Unit,
) : CircuitUiState

internal class HomepageActionPresenter(
    private val smsRepository: SmsRepository,
) : Presenter<HomepageActionState> {

    @Composable
    override fun present(): HomepageActionState {
        return HomepageActionState(
            onClick = {

            }
        )
    }
}
