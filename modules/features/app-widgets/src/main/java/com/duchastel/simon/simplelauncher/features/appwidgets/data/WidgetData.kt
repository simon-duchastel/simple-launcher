package com.duchastel.simon.simplelauncher.features.appwidgets.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class WidgetData(
    val widgetId: Int,
    val providerComponentName: String,
    val width: Int,
    val height: Int,
    val label: String? = null
) : Parcelable

@Serializable
@Parcelize
data class WidgetProviderInfo(
    val componentName: String,
    val label: String,
    val previewImage: String? = null,
    val minWidth: Int,
    val minHeight: Int,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
    val resizeMode: Int = 0,
    val hasConfigurationActivity: Boolean = false
) : Parcelable