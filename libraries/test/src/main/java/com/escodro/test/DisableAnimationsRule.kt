package com.escodro.test

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * [TestRule] to disable the animations during the tests and enable again after finishing.
 */
class DisableAnimationsRule : TestRule {

    private val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

    override fun apply(base: Statement?, description: Description?): Statement =
        object : Statement() {
            override fun evaluate() {
                disableAnimations()
                try {
                    base?.evaluate()
                } finally {
                    enableAnimations()
                }
            }
        }

    private fun disableAnimations() {
        uiDevice.executeShellCommand("settings put global transition_animation_scale 0")
        uiDevice.executeShellCommand("settings put global window_animation_scale 0")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 0")
    }

    private fun enableAnimations() {
        uiDevice.executeShellCommand("settings put global transition_animation_scale 1")
        uiDevice.executeShellCommand("settings put global window_animation_scale 1")
        uiDevice.executeShellCommand("settings put global animator_duration_scale 1")
    }
}
