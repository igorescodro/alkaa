package com.escodro.test

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.adevinta.android.barista.rule.flaky.FlakyTestRule
import org.junit.Rule

/**
 * Allows flaky tests to re-execute if not passing.
 *
 * Unfortunately this is only happening in the CI, while using Gradle Managed Devices and I'm not
 * able to reproduce it locally. So, the silver tape solution here is basically try to execute it
 * again and hope that the Activity is on the right state.
 */
open class FlakyTest {

    @get:Rule(order = 0)
    val scenarioRule = ActivityScenarioRule(ComponentActivity::class.java)

    @get:Rule(order = 1)
    val composeTestRule = createEmptyComposeRule()

    @get:Rule(order = 2)
    val flakyRule = FlakyTestRule().apply { allowFlakyAttemptsByDefault(defaultAttempts = 10) }

    @get:Rule(order = 3)
    val disableAnimationsRule = DisableAnimationsRule()

    /**
     * Loads the Compose content in the UI for testing.
     *
     * @param content the content to be loaded
     */
    fun setContent(content: @Composable () -> Unit) {
        scenarioRule.scenario.onActivity { activity ->
            activity.setContent {
                content()
            }
        }
    }
}
