package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppRepositoryImpl @Inject internal constructor(
    @param:ApplicationContext private val context: Context,
    private val intentLauncher: IntentLauncher,
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
        }.sortedBy { it.label }
    }

    override fun launchApp(app: App): Boolean {
        val intent = packageManager.getLaunchIntentForPackage(app.packageName) ?: return false

        intentLauncher.startActivity(intent)
        return true
    }

    override fun launchAppSystemSettings(app: App): Boolean {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", app.packageName, null)
        }
        intentLauncher.startActivity(intent)
        return true
    }
}
