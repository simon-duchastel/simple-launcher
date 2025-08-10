package com.duchastel.simon.simplelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageScreen
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
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

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsRepository.activityOnCreate()

        setContent {
            CircuitCompositionLocals(circuit) {
                SimpleLauncherTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Color.Transparent, // transparent to allow wallpaper to display
                    ) {
                        val scaffoldState = rememberBottomSheetScaffoldState(
                            bottomSheetState = rememberStandardBottomSheetState(
                                initialValue = SheetValue.Hidden,
                                skipHiddenState = false
                            )
                        )
                        BottomSheetScaffold(
                            scaffoldState = scaffoldState,
                            sheetContent = {
                                CircuitContent(AppListScreen)
                            }
                        ) {
                            CircuitContent(HomepageScreen)
                        }
                    }
                }
            }
        }
    }
}