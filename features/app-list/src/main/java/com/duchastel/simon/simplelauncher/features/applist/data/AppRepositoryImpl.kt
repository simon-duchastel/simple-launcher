package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.duchastel.simon.simplelauncher.features.applist.ui.AppInfo

class AppRepositoryImpl(private val context: Context) : AppRepository {

    private val packageManager: PackageManager = context.packageManager

    override fun getInstalledApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        return packageManager.queryIntentActivities(intent, 0).map {
            AppInfo(
                label = it.loadLabel(packageManager).toString(),
                packageName = it.activityInfo.packageName,
                icon = it.loadIcon(packageManager)
            )
        }
    }

    override fun launchApp(packageName: String): Intent? {
        return packageManager.getLaunchIntentForPackage(packageName)
    }
}
