package com.escodro.alkaa.framework

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.action.ViewActions.pressImeActionButton
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.RootMatchers.isDialog
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher

/**
 * Handles all the test events.
 */
@Suppress("UndocumentedPublicFunction")
class Events {

    private val context = InstrumentationRegistry.getTargetContext()

    fun clickOnView(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(click())
    }

    fun clickOnViewWithText(@StringRes resId: Int) {
        onView(withText(resId)).perform(click())
    }

    fun clickOnRecyclerItem(@IdRes recyclerView: Int) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

    fun longPressOnRecyclerItem(@IdRes recyclerView: Int) {
        onView(withId(recyclerView)).perform(
            RecyclerViewActions
                .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, longClick())
        )
    }

    fun clickDialogOption(@StringRes stringResource: Int, index: Int) {
        val dialogOption = context.resources.getStringArray(stringResource)[index]

        onView(withText(dialogOption))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
            .perform(click())
    }

    fun textOnView(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).perform(typeText(text))
    }

    fun pressImeActionButton(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(pressImeActionButton())
    }

    fun waitFor(@IdRes viewId: Int, delay: Long) {
        onView(isRoot()).perform(waitId(viewId, delay))
    }

    private fun waitId(@IdRes viewId: Int, delay: Long): ViewAction =
        object : ViewAction {

            override fun getDescription(): String {
                return "wait for a specific view with id [$viewId} during [$delay] millis."
            }

            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun perform(uiController: UiController?, view: View?) {
                uiController?.loopMainThreadForAtLeast(delay)
            }
        }
}
