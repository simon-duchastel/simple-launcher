package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.duchastel.simon.simplelauncher.features.sms.data.SmsRepository
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@Parcelize
object HomepageActionScreen : Screen

data class HomepageActionState(
    val onClick: () -> Unit,
) : CircuitUiState

class HomepageActionPresenter @Inject internal constructor(
    private val smsRepository: SmsRepository,
) : Presenter<HomepageActionState> {

    @Composable
    override fun present(): HomepageActionState {
        val coroutineScope = rememberCoroutineScope()
        return HomepageActionState(
            onClick = {
                coroutineScope.launch {
                    // TODO - replace this with a real call to the repository
                    delay(500.milliseconds)
//                    val smsSent = smsRepository.sendSms(
//                        "",
//                        "Test 123",
//                    )
                }
            }
        )
    }
}
