package com.duchastel.simon.simplelauncher.features.applist

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duchastel.simon.simplelauncher.features.applist.ui.AppList
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppListTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appList_displaysApps() {
        composeTestRule.setContent {
            AppList()
        }

        // Verify that some common app names are displayed (you might need to adjust these)
        composeTestRule.onNodeWithText("Settings").assertExists()
        composeTestRule.onNodeWithText("Phone").assertExists()
    }

    @Test
    fun appList_clickLaunchesApp() {
        composeTestRule.setContent {
            AppList()
        }

        // Click on an app (e.g., Settings) and verify it launches (this is a simplified check)
        composeTestRule.onNodeWithText("Settings").performClick()
        // In a real test, you would verify that the Settings app was launched.
        // This might involve checking logs, or using UiAutomator for cross-app testing.
    }
}
