package com.duchastel.simon.simplelauncher.features.appwidgets.host

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LauncherAppWidgetHost @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AppWidgetHost(context, HOST_ID) {

    companion object {
        private const val HOST_ID = 1024
    }

    override fun onCreateView(
        context: Context,
        appWidgetId: Int,
        appWidget: AppWidgetProviderInfo?
    ): AppWidgetHostView {
        return LauncherAppWidgetHostView(context)
    }

    fun allocateWidgetId(): Int {
        return allocateAppWidgetId()
    }

    fun deallocateWidgetId(widgetId: Int) {
        deleteAppWidgetId(widgetId)
    }

    override fun startListening() {
        super.startListening()
    }

    override fun stopListening() {
        super.stopListening()
    }
}