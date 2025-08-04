package com.duchastel.simon.simplelauncher.features.applist.data

/**
 * Repository for managing apps on the device.
 */
interface AppRepository {
    /**
     * Returns a list of [App] objects representing all installed applications
     * that should be displayed (ie. all launchable apps).
     */
    fun getInstalledApps(): List<App>

    /**
     * Launches the provided [App] on the device.
     *
     * @param app The app to be launched.
     */
    fun launchApp(app: App)
}
