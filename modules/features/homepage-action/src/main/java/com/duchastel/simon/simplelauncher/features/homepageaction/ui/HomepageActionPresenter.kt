package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomepageActionButton(
    val smsDestination: PhoneNumber,
    val emoji: String,
) : Screen

typealias PhoneNumber = String

data class HomepageActionState(
    val emoji: String,
    val onClick: () -> Unit,
) : CircuitUiState

class HomepageActionPresenter @AssistedInject internal constructor(
    @Assisted private val config: HomepageActionButton,
    private val smsRepository: SmsRepository,
) : Presenter<HomepageActionState> {

    @Composable
    override fun present(): HomepageActionState {
        val coroutineScope = rememberCoroutineScope()
        return HomepageActionState(
            emoji = config.emoji,
            onClick = {
                coroutineScope.launch {
                    val smsSent = smsRepository.sendSms(
                        config.smsDestination,
                        config.emoji.toString(),
                    )
                }
            }
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(config: HomepageActionButton): HomepageActionPresenter
    }
}
