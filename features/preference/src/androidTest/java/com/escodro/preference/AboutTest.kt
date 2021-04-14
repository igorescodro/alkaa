package com.escodro.preference

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.designsystem.AlkaaTheme
import com.escodro.preference.presentation.About
import org.junit.Rule
import org.junit.Test

internal class AboutTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Test
    fun test_informationIsDisplayed() {
        // When the view is loaded
        loadView()

        // Then the items are shown
        val appName = context.getString(R.string.app_name)
        val description = context.getString(R.string.about_description)
        val button = context.getString(R.string.about_button_project)
        composeTestRule.onNodeWithText(text = appName, ignoreCase = true).assertExists()
        composeTestRule.onNodeWithText(text = description, useUnmergedTree = true).assertExists()
        composeTestRule.onNodeWithText(text = button, useUnmergedTree = true).assertExists()
    }

    private fun loadView() {
        composeTestRule.setContent {
            AlkaaTheme {
                About(onUpPress = { })
            }
        }
    }
}
