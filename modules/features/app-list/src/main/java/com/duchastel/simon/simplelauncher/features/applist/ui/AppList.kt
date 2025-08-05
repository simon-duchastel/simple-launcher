package com.duchastel.simon.simplelauncher.features.applist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.libs.ui.components.SettingsButton
import com.google.accompanist.drawablepainter.rememberDrawablePainter

@Composable
internal fun AppList(state: AppListState, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                SettingsButton(
                    onClick = { state.onSettingsClicked() },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(32.dp)
                )
            }
        }
        items(state.apps) { app ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { app.launchApp() }
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
}
