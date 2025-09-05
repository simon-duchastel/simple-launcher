package com.duchastel.simon.simplelauncher.features.settings.data

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.settings.data.Setting.HomepageAction
import com.duchastel.simon.simplelauncher.features.settings.data.Setting.WidgetConfiguration
import kotlinx.serialization.Serializable

/**
 * Data for each of the [Setting] types.
 */
@Serializable
sealed interface SettingData {

    /**
     * Data associated with the [HomepageAction] setting.
     */
    @Serializable
    data class HomepageActionSettingData(
        val emoji: String,
        val phoneNumber: String,
    ): SettingData
    
    /**
     * Data associated with the [WidgetConfiguration] setting.
     */
    @Serializable
    data class WidgetConfigurationSettingData(
        val widgetData: WidgetData?
    ): SettingData
}

fun SettingData.toSetting(): Setting {
    return when (this) {
        is SettingData.HomepageActionSettingData -> HomepageAction
        is SettingData.WidgetConfigurationSettingData -> WidgetConfiguration
    }
}