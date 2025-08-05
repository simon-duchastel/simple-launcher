package com.duchastel.simon.simplelauncher.features.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.duchastel.simon.simplelauncher.features.settings.ui.SettingsScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                CircuitCompositionLocals(circuit) {
                    val backstack = rememberSaveableBackStack(SettingsScreen)
                    val navigator = rememberCircuitNavigator(backstack)
                    NavigableCircuitContent(navigator, backstack)
                }
            }
        }
    }
}