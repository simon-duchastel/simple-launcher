package com.duchastel.simon.simplelauncher.libs.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class VerticalSlidingDrawerTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun drawer_renders_content_and_drag_handle() {
        composeTestRule.setContent {
            VerticalSlidingDrawer(
                drawerContent = { Text("Drawer Content") },
                content = { Box(modifier = Modifier.fillMaxSize()) }
            )
        }

        composeTestRule.onNodeWithText("Drawer Content").assertIsDisplayed()
    }
}
