package com.duchastel.simon.simplelauncher

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageScreen
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import com.duchastel.simon.simplelauncher.ui.theme.SimpleLauncherTheme
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.CircuitContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CircuitCompositionLocals(circuit) {
                SimpleLauncherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        val pagerState = rememberPagerState(pageCount = { 2 })
                        VerticalPager(
                            state = pagerState,
                            modifier = Modifier
                        ) { page ->
                            when (page) {
                                0 -> CircuitContent(HomepageScreen())
                                1 -> CircuitContent(AppListScreen)
                            }
                        }
                    }
                }
            }
        }
    }
}