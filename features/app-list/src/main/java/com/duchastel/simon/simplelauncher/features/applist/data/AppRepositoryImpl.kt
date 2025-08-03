package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppRepositoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context,
) : AppRepository {

    private val packageManager: PackageManager = context.packageManager

    override fun getInstalledApps(): List<App> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val activities = packageManager.queryIntentActivities(intent, 0)
        return activities.map { resolveInfo ->
            App(
                label = resolveInfo.loadLabel(packageManager).toString(),
                packageName = resolveInfo.activityInfo.packageName,
                icon = resolveInfo.loadIcon(packageManager)
            )
        }
    }

    override fun launchApp(app: App) {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName)
        context.startActivity(intent)
    }
}
