package com.duchastel.simon.simplelauncher.features.settings.data

import com.duchastel.simon.simplelauncher.features.settings.data.Setting.HomepageAction

/**
 * Data for each of the [Setting] types.
 */
sealed interface SettingData {

    /**
     * Data associated with the [HomepageAction] setting.
     */
    data class HomepageActionSettingData(
        val emoji: String,
        val phoneNumber: String,
    ): SettingData
}

fun SettingData.toSetting(): Setting {
    return when (this) {
        is SettingData.HomepageActionSettingData -> HomepageAction
    }
}