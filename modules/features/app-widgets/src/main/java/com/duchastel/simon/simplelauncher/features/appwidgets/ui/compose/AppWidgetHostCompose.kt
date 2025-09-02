package com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose

import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.host.LauncherAppWidgetHost

@Composable
fun AppWidgetHostCompose(
    widgetData: WidgetData,
    appWidgetHost: LauncherAppWidgetHost,
    modifier: Modifier = Modifier,
    onError: ((Throwable) -> Unit)? = null
) {
    val context = LocalContext.current
    var hostView by remember(widgetData.widgetId) { mutableStateOf<AppWidgetHostView?>(null) }
    var isLoading by remember(widgetData.widgetId) { mutableStateOf(true) }
    var error by remember(widgetData.widgetId) { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(widgetData.widgetId) {
        try {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val providerInfo = appWidgetManager.getAppWidgetInfo(widgetData.widgetId)
            
            if (providerInfo != null) {
                val view = appWidgetHost.createView(context, widgetData.widgetId, providerInfo)
                hostView = view
                isLoading = false
            } else {
                throw IllegalStateException("Widget provider info not found for ID: ${widgetData.widgetId}")
            }
        } catch (e: Exception) {
            error = e
            isLoading = false
            onError?.invoke(e)
        }
    }

    DisposableEffect(widgetData.widgetId) {
        onDispose {
            // Clean up the host view when the composable is disposed
            hostView = null
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            error != null -> {
                Text(
                    text = "Widget loading failed: ${error?.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            hostView != null -> {
                AndroidView(
                    factory = { hostView!! },
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
            }
            
            else -> {
                Text(
                    text = "No widget to display",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}