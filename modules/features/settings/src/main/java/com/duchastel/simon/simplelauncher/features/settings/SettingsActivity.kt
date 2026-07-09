package com.duchastel.simon.simplelauncher.features.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.settings.ui.settings.SettingsScreen
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var permissionsRepository: PermissionsRepository

    @Inject
    lateinit var contactsRepository: ContactsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        permissionsRepository.activityOnCreate()
        contactsRepository.activityOnCreate()

        setContent {
            CircuitCompositionLocals(circuit) {
                SimpleLauncherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.systemBars)
                        ) {
                            val backstack = rememberSaveableBackStack(SettingsScreen)
                            val navigator = rememberCircuitNavigator(backstack)
                            val decorationFactory = remember(navigator) {
                                GestureNavigationDecorationFactory(onBackInvoked = navigator::pop)
                            }
                            NavigableCircuitContent(
                                navigator = navigator,
                                backStack = backstack,
                                decoratorFactory = decorationFactory,
                            )
                        }
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