package com.duchastel.simon.simplelauncher.features.applist.data

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import com.duchastel.simon.simplelauncher.intents.IntentLauncher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.eq
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.verify

class AppRepositoryImplTest {
    private lateinit var context: Context
    private lateinit var packageManager: PackageManager
    private lateinit var intentLauncher: IntentLauncher
    private lateinit var appRepository: AppRepositoryImpl

    @Before
    fun setUp() {
        context = mock()
        packageManager = mock()
        intentLauncher = mock()
        whenever(context.packageManager).doReturn(packageManager)

        appRepository = AppRepositoryImpl(context, intentLauncher)
    }

    @Ignore("TODO - fix the mocking issue in this test")
    @Test
    fun `getInstalledApps returns mapped apps`() {
        val activityInfo = mock<ActivityInfo>()
        val icon = mock<Drawable>()
        whenever(activityInfo.packageName).thenReturn("com.example.test")

        val resolveInfo = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence {
                return "Test App"
            }

            override fun loadIcon(pm: PackageManager): Drawable {
                return icon
            }
        }
        resolveInfo.activityInfo = activityInfo
        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(listOf(resolveInfo))

        val result = appRepository.getInstalledApps()
        assertEquals(1, result.size)
        assertEquals("Test App", result[0].label)
        assertEquals("com.example.test", result[0].packageName)
        assertEquals(icon, result[0].icon)
    }

    @Test
    fun `launchApp starts correct intent`() {
        val app = App(label = "App1", packageName = "com.example.app1", icon = mock())
        val intent = mock<Intent>()
        whenever(packageManager.getLaunchIntentForPackage("com.example.app1")).thenReturn(intent)

        appRepository.launchApp(app)

        verify(intentLauncher).startActivity(intent)
    }
}
