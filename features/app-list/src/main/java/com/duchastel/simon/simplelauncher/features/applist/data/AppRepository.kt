package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Intent
import com.duchastel.simon.simplelauncher.features.applist.data.AppInfo

interface AppRepository {
    fun getInstalledApps(): List<AppInfo>
    fun launchApp(packageName: String): Intent?
}
