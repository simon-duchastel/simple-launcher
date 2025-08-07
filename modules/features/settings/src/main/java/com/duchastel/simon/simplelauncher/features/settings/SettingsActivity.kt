package com.duchastel.simon.simplelauncher.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.duchastel.simon.simplelauncher.features.settings.ui.SettingsScreen
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var permissionsRepository: PermissionsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsRepository.activityOnCreate()

        setContent {
            CircuitCompositionLocals(circuit) {
                SimpleLauncherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        val backstack = rememberSaveableBackStack(SettingsScreen)
                        val navigator = rememberCircuitNavigator(backstack)
                        NavigableCircuitContent(navigator, backstack)
                    }
                }
            }
        }
    }

    companion object {
        /**
         * Create an intent for launching this activity.
         */
        fun newActivityIntent(@ApplicationContext context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }
}