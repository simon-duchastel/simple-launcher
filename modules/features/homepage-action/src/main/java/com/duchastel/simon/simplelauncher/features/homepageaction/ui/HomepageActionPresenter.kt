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
import java.time.Duration

@Parcelize
data class HomepageActionButton(
    val smsDestination: PhoneNumber,
    val emoji: String,
) : Screen

typealias PhoneNumber = String

data class HomepageActionState(
    val emoji: String,
    val onLongClick: (Duration) -> Unit,
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
                    smsRepository.sendSms(
                        config.smsDestination,
                        config.emoji,
                    )
                }
            },
            onLongClick = { duration ->
                coroutineScope.launch {
                    val count = (duration.toMillis() / 500).toInt()
                    smsRepository.sendSms(
                        config.smsDestination,
                        config.emoji.repeat(count.coerceAtLeast(1)),
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
