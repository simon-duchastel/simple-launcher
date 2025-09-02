package com.duchastel.simon.simplelauncher.features.appwidgets.host

import android.appwidget.AppWidgetHostView
import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.WidgetDefaultView
import com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose.WidgetErrorView
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme

class LauncherAppWidgetHostView(context: Context) : AppWidgetHostView(context) {

    override fun getDefaultView(): View {
        return ComposeView(context).apply {
            setContent {
                SimpleLauncherTheme {
                    WidgetDefaultView()
                }
            }
        }
    }

    override fun getErrorView(): View {
        return ComposeView(context).apply {
            setContent {
                SimpleLauncherTheme {
                    WidgetErrorView(error = WidgetError.Unknown("Widget failed to load"))
                }
            }
        }
    }
}