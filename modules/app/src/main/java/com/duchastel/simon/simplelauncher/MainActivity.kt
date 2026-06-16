package com.duchastel.simon.simplelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListScreen
import com.duchastel.simon.simplelauncher.features.homepage.ui.HomepageScreen
import com.duchastel.simon.simplelauncher.libs.contacts.data.ContactsRepository
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

    @Inject
    lateinit var contactsRepository: ContactsRepository

    @OptIn(ExperimentalMaterial3Api::class)
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
                        val sheetState = rememberModalBottomSheetState(
                            skipPartiallyExpanded = true,
                        )
                        var showBottomSheet by remember { mutableStateOf(false) }

                        LaunchedEffect(showBottomSheet) {
                            if (showBottomSheet) {
                                sheetState.show()
                            }
                        }

                        LaunchedEffect(sheetState.currentValue) {
                            if (sheetState.currentValue == SheetValue.Hidden && showBottomSheet) {
                                showBottomSheet = false
                            }
                        }

                        Box(modifier = Modifier.fillMaxSize()) {
                            // Homepage with swipe-up detection to open drawer
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(showBottomSheet) {
                                        if (showBottomSheet) return@pointerInput
                                        var totalDrag = 0f
                                        detectVerticalDragGestures(
                                            onDragStart = { totalDrag = 0f },
                                            onDragEnd = { totalDrag = 0f },
                                            onVerticalDrag = { change, dragAmount ->
                                                totalDrag += dragAmount
                                                if (totalDrag < -150f) {
                                                    showBottomSheet = true
                                                    change.consume()
                                                }
                                            }
                                        )
                                    }
                            ) {
                                CircuitContent(HomepageScreen)
                            }

                            if (showBottomSheet) {
                                ModalBottomSheet(
                                    onDismissRequest = { showBottomSheet = false },
                                    sheetState = sheetState,
                                    scrimColor = Color.Transparent,
                                    dragHandle = { BottomSheetDefaults.DragHandle() },
                                    shape = BottomSheetDefaults.ExpandedShape,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                ) {
                                    CircuitContent(AppListScreen)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
