package com.duchastel.simon.simplelauncher.features.appwidgets.host

import android.appwidget.AppWidgetHostView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

class LauncherAppWidgetHostView(context: Context) : AppWidgetHostView(context) {

    override fun getDefaultView(): View {
        // Return a default view when widget loading fails
        return TextView(context).apply {
            text = "Widget loading failed"
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(16, 16, 16, 16)
        }
    }

    override fun getErrorView(): View {
        // Return an error view when widget has an error
        return TextView(context).apply {
            text = "Widget error"
            textAlignment = View.TEXT_ALIGNMENT_CENTER
            setPadding(16, 16, 16, 16)
        }
    }
}