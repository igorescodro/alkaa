package com.escodro.alkaa

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.escodro.alkaa.test.AlkaaBaseTest
import com.escodro.alkaa.test.afterTest
import com.escodro.alkaa.test.beforeTest
import com.escodro.alkaa.test.uiTest
import org.koin.test.KoinTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
internal class PreferenceFlowTest : AlkaaBaseTest(), KoinTest {

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

    private fun ComposeUiTest.navigateToPreferences() {
        onNodeWithContentDescription(label = "More", useUnmergedTree = true).performClick()
    }
}
