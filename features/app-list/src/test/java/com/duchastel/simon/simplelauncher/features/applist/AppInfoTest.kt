package com.duchastel.simon.simplelauncher.features.applist

import android.graphics.drawable.Drawable
import com.duchastel.simon.simplelauncher.features.applist.ui.AppInfo
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.mock

class AppInfoTest {

    @Test
    fun testAppInfo() {
        val label = "My App"
        val packageName = "com.example.myapp"
        val icon = mock(Drawable::class.java)

        val appInfo = AppInfo(label, packageName, icon)

        assertEquals(label, appInfo.label)
        assertEquals(packageName, appInfo.packageName)
        assertEquals(icon, appInfo.icon)
    }
}
