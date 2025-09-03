package com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose

import android.appwidget.AppWidgetHostView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState

@Composable
fun AppWidgetHostCompose(
    widgetData: WidgetData,
    widgetViewState: WidgetViewState,
    widgetHostView: AppWidgetHostView?,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (widgetViewState) {
            is WidgetViewState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            is WidgetViewState.Error -> {
                WidgetErrorView(
                    error = widgetViewState.error,
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            is WidgetViewState.Success -> {
                if (widgetHostView != null) {
                    AndroidView(
                        factory = { widgetHostView },
                        modifier = Modifier.fillMaxSize(),
                        update = { view ->
                            // Update widget size if needed
                            val layoutParams = view.layoutParams
                            if (layoutParams != null) {
                                layoutParams.width = widgetData.width
                                layoutParams.height = widgetData.height
                                view.layoutParams = layoutParams
                            }
                        }
                    )
                } else {
                    WidgetErrorView(
                        error = WidgetError.Unknown("Widget view not available"),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}