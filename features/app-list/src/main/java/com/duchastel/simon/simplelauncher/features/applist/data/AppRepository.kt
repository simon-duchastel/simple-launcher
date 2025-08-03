package com.duchastel.simon.simplelauncher.features.applist.data

/**
 *
 */
interface AppRepository {
    /**
     *
     */
    fun getInstalledApps(): List<App>

    /**
     *
     */
    fun launchApp(app: App)
}
