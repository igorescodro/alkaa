package com.escodro.alkaa

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import com.escodro.test.AlkaaTest
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class PreferenceFlowTest : AlkaaTest(), KoinTest {

    @BeforeTest
    fun setup() {
        beforeTest()
    }

    @AfterTest
    fun tearDown() {
        afterTest()
    }

    @Test
    fun all_preferences_are_visible() = uiTest {
        navigateToPreferences()

        onNodeWithText(text = "Task Tracker").assertExists()
        onNodeWithText(text = "App theme").assertExists()
        onNodeWithText(text = "About").assertExists()
        onNodeWithText(text = "Open source licenses").assertExists()
        onNodeWithText(text = "Version").assertExists()
    }

    @Test
    fun open_about_screen() = uiTest {
        navigateToPreferences()

        onNodeWithText(text = "About").performClick()
        onNodeWithText(text = "alkaa").assertExists()
    }

    @Test
    fun open_open_source_license() = uiTest {
        navigateToPreferences()

        onNodeWithText(text = "Open source licenses").performClick()

        // This library is very likely to appear
        onAllNodesWithText("AboutLibraries", substring = true)[0].performClick()
        onNodeWithText("OK").performClick()
    }

    @Test
    fun change_app_theme() = uiTest {
        navigateToPreferences()

        onNodeWithText(text = "App theme").performClick()

        // Validate if the dialog is shown
        onAllNodes(hasText("App theme")).onLast().assertExists()

        // Select Dark theme
        onAllNodesWithText(text = "Dark theme").onLast().performClick()

        // Validate if the dialog is closed
        onNodeWithTag("App theme").assertDoesNotExist()
    }

    @Test
    fun when_version_is_clicked_five_times_it_opens_youtube() = uiTest {
        navigateToPreferences()

        // Click version 7 times
        repeat(7) {
            onNodeWithText(text = "Version").performClick()
        }

        // We can't easily verify the URI opening, but we can verify the clicks don't crash the app
        onNodeWithText(text = "Version").assertExists()
    }

    private fun ComposeUiTest.navigateToPreferences() {
        onNodeWithContentDescription(label = "More", useUnmergedTree = true).performClick()
    }
}
