package com.escodro.preference

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.model.AppThemeOptions
import com.escodro.preference.presentation.PreferenceContent
import org.junit.Rule
import org.junit.Test

internal class PreferenceSectionTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_allItemsAreDisplayed() {
        // When the view is loaded
        loadView()

        // Then the items are shown
        val itemTracker = context.getString(R.string.preference_title_tracker)
        val itemTheme = context.getString(R.string.preference_title_app_theme)
        val itemAbout = context.getString(R.string.preference_title_about)
        val itemVersion = context.getString(R.string.preference_title_version)

        composeTestRule.onNodeWithText(text = itemTracker, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(text = itemAbout, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(text = itemVersion, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(text = itemTheme, useUnmergedTree = true).assertExists()
    }

    private fun loadView() {
        composeTestRule.setContent {
            AlkaaTheme {
                PreferenceContent(
                    onAboutClick = { },
                    onTrackerClick = { },
                    theme = AppThemeOptions.SYSTEM,
                    onThemeUpdate = { }
                )
            }
        }
    }
}
