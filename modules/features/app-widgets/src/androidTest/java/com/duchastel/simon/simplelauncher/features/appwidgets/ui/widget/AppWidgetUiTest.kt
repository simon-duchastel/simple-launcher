package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.duchastel.simon.simplelauncher.libs.ui.theme.SimpleLauncherTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppWidgetUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // Note: In real tests, we would use a real AppWidgetHostView or test without it
    private val widgetData = WidgetData(
        widgetId = 123,
        providerComponentName = "com.example/ExampleWidget",
        width = 200,
        height = 100,
        label = "Test Widget"
    )

    @Test
    fun appWidgetUi_displaysErrorStateWithRetryAndRemoveButtons() {
        var retryClicked = false
        var removeClicked = false

        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Error(WidgetError.ProviderNotFound),
            widgetHostView = null,
            onRetry = { retryClicked = true },
            onRemoveWidget = { removeClicked = true }
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        // Verify error message is displayed
        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Widget provider not found").assertIsDisplayed()
        
        // Verify buttons are displayed
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNodeWithText("Remove Widget").assertIsDisplayed()
        
        // Test retry button click
        composeTestRule.onNodeWithText("Retry").performClick()
        assert(retryClicked)
        
        // Test remove widget button click
        composeTestRule.onNodeWithText("Remove Widget").performClick()
        assert(removeClicked)
    }

    @Test
    fun appWidgetUi_displaysErrorStateForPermissionDenied() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Error(WidgetError.PermissionDenied),
            widgetHostView = null
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Widget permission denied").assertIsDisplayed()
    }

    @Test
    fun appWidgetUi_displaysErrorStateForHostCreationFailed() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Error(WidgetError.HostCreationFailed),
            widgetHostView = null
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to create widget").assertIsDisplayed()
    }

    @Test
    fun appWidgetUi_displaysErrorStateForWidgetBindingFailed() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Error(WidgetError.WidgetBindingFailed),
            widgetHostView = null
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Failed to bind widget").assertIsDisplayed()
    }

    @Test
    fun appWidgetUi_displaysErrorStateForUnknownError() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Error(WidgetError.Unknown("Custom error message")),
            widgetHostView = null
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        composeTestRule.onNodeWithText("Widget Error").assertIsDisplayed()
        composeTestRule.onNodeWithText("Custom error message").assertIsDisplayed()
    }

    @Test
    fun appWidgetUi_displaysSuccessStateWithAppWidgetHostCompose() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Success(widgetData),
            widgetHostView = null // Testing without actual host view
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        // For success state, the AppWidgetHostCompose should be displayed
        // We can't directly test the AndroidView content, but we can verify
        // that error content is not displayed
        composeTestRule.onNodeWithText("Widget Error").assertDoesNotExist()
        composeTestRule.onNodeWithText("Retry").assertDoesNotExist()
        composeTestRule.onNodeWithText("Remove Widget").assertDoesNotExist()
    }

    @Test
    fun appWidgetUi_displaysLoadingStateWithAppWidgetHostCompose() {
        val state = AppWidgetState(
            widgetData = widgetData,
            widgetViewState = WidgetViewState.Loading,
            widgetHostView = null // Testing without actual host view
        )

        composeTestRule.setContent {
            SimpleLauncherTheme {
                AppWidgetUi(state = state)
            }
        }

        // For loading state, the AppWidgetHostCompose should be displayed
        // We can't directly test the AndroidView content, but we can verify
        // that error content is not displayed
        composeTestRule.onNodeWithText("Widget Error").assertDoesNotExist()
        composeTestRule.onNodeWithText("Retry").assertDoesNotExist()
        composeTestRule.onNodeWithText("Remove Widget").assertDoesNotExist()
    }
}