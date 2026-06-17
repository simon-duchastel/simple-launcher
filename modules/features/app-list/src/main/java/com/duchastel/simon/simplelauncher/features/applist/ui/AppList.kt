package com.duchastel.simon.simplelauncher.features.applist.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.libs.ui.components.SettingsButton
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun AppList(state: AppListState, modifier: Modifier) {
    var settingsVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        settingsVisible = true
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.apps) { app ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { app.onClicked() },
                            onLongClick = { app.onLongClicked() }
                        )
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberDrawablePainter(app.icon),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp)
                    ) {
                        Text(text = app.label)
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = settingsVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1200)),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            SettingsButton(
                onClick = { state.onSettingsClicked() },
            )
        }
    }
}
