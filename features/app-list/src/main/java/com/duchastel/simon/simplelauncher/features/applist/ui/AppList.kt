package com.duchastel.simon.simplelauncher.features.applist.ui

import android.graphics.drawable.ColorDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import kotlinx.collections.immutable.toImmutableList

@Composable
internal fun AppList(state: AppListState) {
    LazyColumn {
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

internal class AppListStateProvider : PreviewParameterProvider<AppListState> {
    override val values: Sequence<AppListState>
        get() = sequenceOf(
            // A list with a few apps
            AppListState(
                apps = listOf(
                    AppListState.App(
                        label = "App 1",
                        icon = ColorDrawable(android.graphics.Color.BLUE),
                        launchApp = {}
                    ),
                    AppListState.App(
                        label = "App 2",
                        icon = ColorDrawable(android.graphics.Color.RED),
                        launchApp = {}
                    ),
                    AppListState.App(
                        label = "App 3",
                        icon = ColorDrawable(android.graphics.Color.GREEN),
                        launchApp = {}
                    )
                ).toImmutableList()
            ),
            // An empty list of apps
            AppListState(
                apps = emptyList<AppListState.App>().toImmutableList()
            )
        )
}

@Preview(showBackground = true)
@Composable
internal fun AppListPreview(
    @PreviewParameter(AppListStateProvider::class) state: AppListState
) {
    AppList(state)
}
