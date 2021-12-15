package com.escodro.preference

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.model.AppThemeOptions
import com.escodro.preference.presentation.PreferenceContent
import org.junit.Rule
import org.junit.Test

internal class ThemePreferenceTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_themeIsBeingUpdated() {
        // When the view is loaded
        loadView()

        // Then click in all the theme options and verify it is updated
        val itemTheme = context.getString(R.string.preference_title_app_theme)

        AppThemeOptions.values().forEach { theme ->
            composeTestRule.onNodeWithText(itemTheme).performClick()

            val title = context.getString(theme.titleRes)
            composeTestRule.onNodeWithText(text = title, useUnmergedTree = true).performClick()
            composeTestRule.onNodeWithText(text = title, useUnmergedTree = true).assertExists()
        }
    }

    private fun loadView() {
        composeTestRule.setContent {
            var theme by remember { mutableStateOf(AppThemeOptions.SYSTEM) }

            AlkaaTheme {
                PreferenceContent(
                    onAboutClick = { },
                    onTrackerClick = { },
                    theme = theme,
                    onThemeUpdate = { theme = it }
                )
            }
        }
    }
}
