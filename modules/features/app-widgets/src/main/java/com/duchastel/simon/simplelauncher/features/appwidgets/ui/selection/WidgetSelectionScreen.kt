package com.duchastel.simon.simplelauncher.features.appwidgets.ui.selection

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class WidgetSelectionScreen(
    val currentWidget: WidgetData? = null
) : Screen