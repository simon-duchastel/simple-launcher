package com.duchastel.simon.simplelauncher.features.appwidgets.ui.compose

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WidgetErrorViewsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun widgetDefaultView_displaysLoadingText() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetDefaultView()
            }
        }

        composeTestRule.onNodeWithText("Widget loading...").assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysProviderNotFoundError() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.ProviderNotFound)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Widget provider not found").assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysPermissionDeniedError() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.PermissionDenied)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Widget permission denied").assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysHostCreationFailedError() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.HostCreationFailed)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to create widget").assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysWidgetBindingFailedError() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.WidgetBindingFailed)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to bind widget").assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysUnknownErrorWithMessage() {
        val errorMessage = "Custom error occurred"
        
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.Unknown(errorMessage))
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
    }

    @Test
    fun widgetErrorView_displaysUnknownErrorWithEmptyMessage() {
        composeTestRule.setContent {
            SimpleLauncherTheme {
                WidgetErrorView(error = WidgetError.Unknown(""))
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("").assertIsDisplayed()
    }
}