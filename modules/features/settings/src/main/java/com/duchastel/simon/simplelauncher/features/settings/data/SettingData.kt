package com.duchastel.simon.simplelauncher.features.settings.data

import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.settings.data.Setting.HomepageAction
import com.duchastel.simon.simplelauncher.features.settings.data.Setting.CenterWidget
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
     * Data associated with the [CenterWidget] setting.
     */
    @Serializable
    data class CenterWidgetSettingData(
        val widgetData: WidgetData,
    ): SettingData
}

fun SettingData.toSetting(): Setting {
    return when (this) {
        is SettingData.HomepageActionSettingData -> HomepageAction
        is SettingData.CenterWidgetSettingData -> CenterWidget
    }
}