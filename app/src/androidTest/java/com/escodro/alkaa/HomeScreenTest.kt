package com.escodro.alkaa

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.model.HomeSection
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.alkaa.util.WindowSizeClassFake
import com.escodro.designsystem.AlkaaTheme
import com.escodro.test.rule.DisableAnimationsRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        composeTestRule.setContent {
            AlkaaTheme {
                NavGraph(windowSizeClass = WindowSizeClassFake.WindowSizeClassPhone)
            }
        }
    }

    @Test
    fun test_titleChangesWhenBottomIconIsSelected() {
        HomeSection.values().forEach { section ->
            val title = context.getString(section.title)

            // Click on each item and validate the title
            composeTestRule.onNodeWithContentDescription(label = title, useUnmergedTree = true)
                .performClick()
            composeTestRule.onAllNodesWithText(title)[0].assertExists()
        }
    }
}
