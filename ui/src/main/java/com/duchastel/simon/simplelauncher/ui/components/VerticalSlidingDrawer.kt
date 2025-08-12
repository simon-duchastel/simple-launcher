package com.duchastel.simon.simplelauncher.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerticalSlidingDrawer(
    content: @Composable () -> Unit,
    drawerContent: @Composable () -> Unit
) {
    val sheetState = rememberStandardBottomSheetState()
    val scope = rememberCoroutineScope()
    rememberSwipeToDismissBoxState()
    Box(
        modifier = Modifier.pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val y = dragAmount.y
                if (y < 0) {
                    scope.launch {
                        sheetState.expand()
                        sheetState.targetValue
                    }
                }
            }
        }
    ) {
        content()
    }
    if (sheetState.isVisible) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { scope.launch { sheetState.hide() } },
        ) {
            drawerContent()
        }
    }
}
