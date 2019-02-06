package com.escodro.alkaa.framework

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.chip.Chip
import org.hamcrest.Matcher

/**
 * Custom [ViewAction]s to be used in tests.
 */
object ViewActions {

    fun waitId(delay: Long): ViewAction =
        object : ViewAction {

            override fun getDescription(): String {
                return "wait for [$delay] millis."
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }
        }

    fun closeChip(): ViewAction =
        object : ViewAction {

            override fun getDescription(): String {
                return "close the chip"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Chip::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val chip = view as Chip
                chip.performCloseIconClick()
            }
        }
}
