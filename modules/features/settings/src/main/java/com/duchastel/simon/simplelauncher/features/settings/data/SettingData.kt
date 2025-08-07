package com.duchastel.simon.simplelauncher.features.settings.data

import com.duchastel.simon.simplelauncher.features.settings.data.Setting.HomepageAction
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
}

fun SettingData.toSetting(): Setting {
    return when (this) {
        is SettingData.HomepageActionSettingData -> HomepageAction
    }
}