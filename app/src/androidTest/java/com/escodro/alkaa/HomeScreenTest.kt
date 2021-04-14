package com.escodro.alkaa

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.model.HomeSection
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.designsystem.AlkaaTheme
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
                NavGraph()
            }
        }
    }

    @Test
    fun test_titleChangesWhenBottomIconIsSelected() {
        HomeSection.values().forEach { section ->
            val title = context.getString(section.title)
            composeTestRule.onNodeWithContentDescription(label = title, useUnmergedTree = true)
                .performClick()
            composeTestRule.onNodeWithText(title).assertExists()
        }
    }
}
