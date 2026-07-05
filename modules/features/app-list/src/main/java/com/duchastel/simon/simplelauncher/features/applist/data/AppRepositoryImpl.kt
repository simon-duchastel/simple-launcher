package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

private data class ComponentIdentifier(
    val packageName: String,
    val className: String,
)

class AppRepositoryImpl @Inject internal constructor(
    @param:ApplicationContext private val context: Context,
    private val intentLauncher: IntentLauncher,
) : AppRepository {

    private val packageManager: PackageManager = context.packageManager

    override fun getInstalledApps(): List<App> {
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
        }

        val launcherApps = packageManager.queryIntentActivities(launcherIntent, 0)
        val homeApps = packageManager.queryIntentActivities(homeIntent, 0)
        val homeComponentNames = homeApps.map { resolveInfo ->
            ComponentIdentifier(
                packageName = resolveInfo.activityInfo.packageName,
                className = resolveInfo.activityInfo.name,
            )
        }.toSet()

        return launcherApps
            .filter { resolveInfo ->
                val componentIdentifier = ComponentIdentifier(
                    packageName = resolveInfo.activityInfo.packageName,
                    className = resolveInfo.activityInfo.name,
                )
                componentIdentifier !in homeComponentNames
            }
            .map { resolveInfo ->
                App(
                    label = resolveInfo.loadLabel(packageManager).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    icon = resolveInfo.loadIcon(packageManager)
                )
            }.sortedBy { it.label.lowercase() }
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
