package com.escodro.alkaa

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.model.HomeSection
import com.escodro.alkaa.presentation.home.Home
import com.escodro.theme.AlkaaTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        composeTestRule.setContent {
            AlkaaTheme {
                Home(onTaskClicked = {})
            }
        }
    }

    @Test
    fun test_assertIfTitleChangesWhenBottomIconIsSelected() {
        HomeSection.values().forEach { section ->
            composeTestRule.onNodeWithContentDescription(context.getString(section.title))
                .performClick()
            composeTestRule.onNodeWithText(context.getString(section.title)).assertExists()
        }
    }
}
