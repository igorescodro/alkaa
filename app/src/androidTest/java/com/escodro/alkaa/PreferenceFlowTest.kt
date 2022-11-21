package com.escodro.alkaa

import androidx.annotation.StringRes
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.platform.app.InstrumentationRegistry
import com.escodro.alkaa.navigation.NavGraph
import com.escodro.core.extension.getVersionName
import com.escodro.test.rule.DisableAnimationsRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.escodro.core.R as CoreR
import com.escodro.preference.R as PrefR

internal class PreferenceFlowTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        composeTestRule.setContent {
            NavGraph()
        }
        navigateToPreferences()
    }

    @Test
    fun test_checkVersion() {
        val currentVersion = context.getVersionName()
        composeTestRule.onNodeWithText(currentVersion).assertExists()
    }

    @Test
    fun test_openAboutScreen() {
        with(composeTestRule) {
            onNodeWithText(string(PrefR.string.preference_title_about)).performClick()
            onNodeWithText(string(CoreR.string.app_name), ignoreCase = true).assertExists()
        }
    }

    @Test
    fun test_openOpenSourceLicense() {
        with(composeTestRule) {
            onNodeWithText(string(PrefR.string.preference_title_open_source)).performClick()

            // This library is very likely to appear
            onAllNodesWithText("AboutLibraries Library")[0].performClick()
            onNodeWithText("OK").performClick()
        }
    }

    private fun navigateToPreferences() {
        composeTestRule.onNodeWithContentDescription(
            label = string(R.string.home_title_settings),
            useUnmergedTree = true
        ).performClick()
    }

    private fun string(@StringRes resId: Int): String =
        context.getString(resId)
}
