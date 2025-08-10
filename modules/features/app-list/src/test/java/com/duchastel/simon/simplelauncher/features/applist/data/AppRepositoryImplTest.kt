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
import android.net.Uri
import android.provider.Settings
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.argumentCaptor
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

    @Test
    fun `getInstalledApps returns mapped and sorted apps`() {
        val icon1 = mock<Drawable>()
        val icon2 = mock<Drawable>()
        val activityInfo1 = ActivityInfo()
        activityInfo1.packageName = "com.example.appb"
        val activityInfo2 = ActivityInfo()
        activityInfo2.packageName = "com.example.appa"

        val resolveInfo1 = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "B App"
            override fun loadIcon(pm: PackageManager): Drawable = icon1
        }
        resolveInfo1.activityInfo = activityInfo1

        val resolveInfo2 = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "A App"
            override fun loadIcon(pm: PackageManager): Drawable = icon2
        }
        resolveInfo2.activityInfo = activityInfo2

        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(listOf(resolveInfo1, resolveInfo2))

        val result = appRepository.getInstalledApps()
        assertEquals(2, result.size)
        assertEquals("A App", result[0].label)
        assertEquals("B App", result[1].label)
    }

    @Test
    fun `launchApp starts correct intent`() {
        val app = App(label = "App1", packageName = "com.example.app1", icon = mock())
        val intent = mock<Intent>()
        whenever(packageManager.getLaunchIntentForPackage("com.example.app1")).thenReturn(intent)

        appRepository.launchApp(app)

        verify(intentLauncher).startActivity(intent)
    }

    @Ignore("TODO - Intent is null in test so always passes, re-enable once fixed")
    @Test
    fun `launchAppSystemSettings starts correct intent`() {
        val app = App(label = "App1", packageName = "com.example.app1", icon = mock())

        appRepository.launchAppSystemSettings(app)

        val expectedIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", "com.example.app1", null),
        )
        verify(intentLauncher).startActivity(argThat { intent ->
            intent.action == expectedIntent.action && intent.data == expectedIntent.data
        })
    }
}
