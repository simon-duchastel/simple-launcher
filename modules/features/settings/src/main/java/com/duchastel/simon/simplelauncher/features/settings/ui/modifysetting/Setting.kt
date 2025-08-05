package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class Setting : Parcelable {
    @Parcelize
    data object HomepageAction : Setting()
}
