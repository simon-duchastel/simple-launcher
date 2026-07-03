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

        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(
            listOf(resolveInfo1, resolveInfo2), // first call: CATEGORY_LAUNCHER
            emptyList(),                        // second call: CATEGORY_HOME (no launchers)
        )

        val result = appRepository.getInstalledApps()
        assertEquals(2, result.size)
        assertEquals("A App", result[0].label)
        assertEquals("B App", result[1].label)
    }

    @Test
    fun `getInstalledApps filters out all launchers including self`() {
        val regularAppIcon = mock<Drawable>()
        val selfIcon = mock<Drawable>()
        val otherLauncherIcon = mock<Drawable>()

        val regularAppInfo = ActivityInfo().apply { packageName = "com.example.regular" }
        val selfInfo = ActivityInfo().apply { packageName = "com.duchastel.simon.simplelauncher" }
        val otherLauncherInfo = ActivityInfo().apply { packageName = "com.other.launcher" }

        val regularResolveInfo = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "Regular App"
            override fun loadIcon(pm: PackageManager): Drawable = regularAppIcon
        }.apply { activityInfo = regularAppInfo }

        val selfResolveInfo = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "SimpleLauncher"
            override fun loadIcon(pm: PackageManager): Drawable = selfIcon
        }.apply { activityInfo = selfInfo }

        val otherLauncherResolveInfo = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "Other Launcher"
            override fun loadIcon(pm: PackageManager): Drawable = otherLauncherIcon
        }.apply { activityInfo = otherLauncherInfo }

        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(
            listOf(regularResolveInfo, selfResolveInfo, otherLauncherResolveInfo), // first call: CATEGORY_LAUNCHER
            listOf(selfResolveInfo, otherLauncherResolveInfo),                     // second call: CATEGORY_HOME
        )

        val result = appRepository.getInstalledApps()
        assertEquals(1, result.size)
        assertEquals("Regular App", result[0].label)
    }

    @Test
    fun `getInstalledApps sorts case-insensitively so lowercase-starting labels are not pushed to the end`() {
        val zoom = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "Zoom"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.example.zoom" } }

        val luckin = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "luckin coffee"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.luckin.client.us" } }

        val mTicket = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "mTicket"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.example.mticket" } }

        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(
            listOf(zoom, luckin, mTicket), // first call: CATEGORY_LAUNCHER (given out-of-order)
            emptyList(),                   // second call: CATEGORY_HOME
        )

        val result = appRepository.getInstalledApps()
        assertEquals(listOf("luckin coffee", "mTicket", "Zoom"), result.map { it.label })
    }

    @Test
    fun `getInstalledApps sorts symbol-only labels after lettered ones and ignores leading symbols`() {
        val symbols = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "*&^"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.example.symbols" } }

        val leadingSymbol = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "*My App"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.example.myapp" } }

        val apple = object : ResolveInfo() {
            override fun loadLabel(pm: PackageManager): CharSequence = "Apple"
            override fun loadIcon(pm: PackageManager): Drawable = mock()
        }.apply { activityInfo = ActivityInfo().apply { packageName = "com.example.apple" } }

        whenever(packageManager.queryIntentActivities(any(), eq(0))).thenReturn(
            listOf(symbols, leadingSymbol, apple), // first call: CATEGORY_LAUNCHER
            emptyList(),                          // second call: CATEGORY_HOME
        )

        val result = appRepository.getInstalledApps()
        assertEquals(listOf("Apple", "*My App", "*&^"), result.map { it.label })
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
