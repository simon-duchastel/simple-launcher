package com.duchastel.simon.simplelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageScreen
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.libs.ui.components.VerticalSlidingDrawer
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @Inject
    lateinit var permissionsRepository: PermissionsRepository

    @Inject
    lateinit var contactsRepository: ContactsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsRepository.activityOnCreate()
        contactsRepository.activityOnCreate()

        setContent {
            CircuitCompositionLocals(circuit) {
                SimpleLauncherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent, // transparent to allow wallpaper to display
                    ) {
                        VerticalSlidingDrawer(
                            modifier = Modifier.fillMaxSize(),
                            drawerContent = { CircuitContent(AppListScreen) },
                        ) {
                            CircuitContent(HomepageScreen)
                        }
                    }
                }
            }
        }
    }
}