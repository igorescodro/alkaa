package com.escodro.alkaa

import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.model.HomeSection
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.designsystem.AlkaaTheme
import com.escodro.test.FlakyTest
import org.junit.Before
import org.junit.Test

internal class HomeScreenTest : FlakyTest() {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        setContent {
            AlkaaTheme {
                NavGraph()
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
            composeTestRule.onNodeWithText(title).assertExists()
        }
    }
}
