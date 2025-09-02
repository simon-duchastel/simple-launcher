package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class AppWidgetScreen(
    val widgetData: WidgetData
) : Screen