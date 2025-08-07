package com.duchastel.simon.simplelauncher.features.applist

import android.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.core.content.ContextCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duchastel.simon.simplelauncher.features.applist.ui.AppList
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListState
import androidx.compose.ui.Modifier
import com.duchastel.simon.simplelauncher.features.applist.ui.AppListState.App
import kotlinx.collections.immutable.toImmutableList
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
            val context = LocalContext.current

            AppList(
                state = AppListState(
                    apps = listOf(
                        App(
                            label = "Settings",
                            icon = ContextCompat.getDrawable(context, R.drawable.star_on)!!,
                            launchApp = { },
                        ),
                        App(
                            label = "Phone",
                            icon = ContextCompat.getDrawable(context, R.drawable.star_on)!!,
                            launchApp = { },
                        )
                    ).toImmutableList()
                ),
                onSettingsClicked = {},
                modifier = Modifier
            )
        }

        composeTestRule.onNodeWithText("Settings").assertExists()
        composeTestRule.onNodeWithText("Phone").assertExists()
    }
}
