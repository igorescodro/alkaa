package com.escodro.test

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.test.core.app.ActivityScenario
import com.adevinta.android.barista.rule.flaky.FlakyTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule

/**
 * Allows flaky tests to re-execute if not passing.
 *
 * Unfortunately this is only happening in the CI, while using Gradle Managed Devices and I'm not
 * able to reproduce it locally. So, the silver tape solution here is basically try to execute it
 * again and hope that the Activity is on the right state.
 */
open class FlakyTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

    @get:Rule
    val flakyRule = FlakyTestRule().apply { allowFlakyAttemptsByDefault(defaultAttempts = 10) }

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    private lateinit var scenario: ActivityScenario<ComponentActivity>

    @Before
    fun createScenario() {
        scenario = ActivityScenario.launch(ComponentActivity::class.java)
    }

    @After
    fun closeScenario() {
        scenario.close()
    }

    /**
     * Loads the Compose content in the UI for testing.
     *
     * @param content the content to be loaded
     */
    fun setContent(content: @Composable () -> Unit) {
        scenario.onActivity { activity ->
            activity.setContent {
                content()
            }
        }
    }
}
