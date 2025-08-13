package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.duchastel.simon.simplelauncher.libs.ui.extensions.LongClickScope
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Parcelize
data class HomepageActionButton(
    val smsDestination: PhoneNumber,
    val emoji: String,
) : Screen

typealias PhoneNumber = String

data class HomepageActionState(
    val emoji: String,
    val emojiButtonState: EmojiButtonState,
) : CircuitUiState {

    sealed interface EmojiButtonState {
        data class Ready(
            val onLongClick: (LongClickScope) -> Unit,
            val onClick: () -> Unit,
            val onDoubleClick: () -> Unit,
        ): EmojiButtonState

        data object Loading: EmojiButtonState
    }
}

class HomepageActionPresenter @AssistedInject internal constructor(
    @Assisted private val config: HomepageActionButton,
    private val smsRepository: SmsRepository,
    private val intentLauncher: IntentLauncher,
) : Presenter<HomepageActionState> {

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun present(): HomepageActionState {
        val coroutineScope = rememberCoroutineScope()
        var emojiIsLoading by remember { mutableStateOf(false) }

        val emojiButtonState = if (emojiIsLoading) {
            HomepageActionState.EmojiButtonState.Loading
        } else {
            HomepageActionState.EmojiButtonState.Ready(
                onClick = {
                    coroutineScope.launch {
                        debounceForAtLeast(
                            onShow = { emojiIsLoading = true },
                            onHide = { emojiIsLoading = false },
                        ) {
                            smsRepository.sendSms(
                                config.smsDestination,
                                config.emoji,
                            )
                            delay(300.milliseconds)
                        }
                    }
                },
                onLongClick = { scope ->
                    coroutineScope.launch {
                        val startTime = Clock.System.now()
                        val success = scope.tryAwaitRelease()
                        val endTime = Clock.System.now()

                        if (success) {
                            val duration = endTime - startTime
                            val count = (duration.inWholeMilliseconds / 500).toInt().coerceAtLeast(1)
                            debounceForAtLeast(
                                onShow = { emojiIsLoading = true },
                                onHide = { emojiIsLoading = false },
                            ) {
                                smsRepository.sendSms(
                                    config.smsDestination,
                                    config.emoji.repeat(count),
                                )
                            }
                        }
                    }
                },
                onDoubleClick = {
                    val intent = Intent(Intent.ACTION_VIEW, "sms:${config.smsDestination}".toUri())
                    intentLauncher.startActivity(intent)
                },
            )
        }
        return HomepageActionState(
            emoji = config.emoji,
            emojiButtonState = emojiButtonState,
        )
    }

    @AssistedFactory
    fun interface Factory {
        fun create(config: HomepageActionButton): HomepageActionPresenter
    }
}

// TODO - move to a utils package and rename to make more sense
@OptIn(ExperimentalTime::class)
private suspend fun <T> debounceForAtLeast(
    timeoutBeforeStarting: Duration = 10.milliseconds,
    timeoutBeforeEnding: Duration = 200.milliseconds,
    onShow: suspend () -> Unit,
    onHide: suspend () -> Unit,
    block: suspend () -> T,
): T = coroutineScope {
    var shownAt: Instant? = null
    val showJob = launch {
        delay(timeoutBeforeStarting)
        onShow()
        shownAt = Clock.System.now()
    }

    val result = try {
        block()
    } finally {
        showJob.cancel()
        shownAt?.let {
            val visibleFor = Clock.System.now() - it
            if (visibleFor < timeoutBeforeEnding) {
                delay(timeoutBeforeEnding - visibleFor)
            }
            onHide()
        }
    }

    return@coroutineScope result
}