package com.escodro.alkaa.framework

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.hasErrorText
import android.support.test.espresso.matcher.ViewMatchers.isChecked
import android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.hamcrest.TypeSafeMatcher

/**
 * Handles all the test matchers.
 */
@Suppress("UndocumentedPublicFunction")
class Matchers {

    private val context = InstrumentationRegistry.getTargetContext()

    fun viewIsCompletelyDisplayed(@IdRes viewId: Int) {
        onView(withId(viewId)).check(matches(isCompletelyDisplayed()))
    }

    fun toolbarContainsTitle(@IdRes toolbarId: Int, @StringRes resId: Int) {
        onView(withText(resId)).check(matches(withParent(withId(toolbarId))))
    }

    fun viewContainsError(@IdRes viewId: Int, @StringRes stringResource: Int) {
        val errorMessage = context.getString(stringResource)
        onView(withId(viewId)).check(matches(hasErrorText(errorMessage)))
    }

    fun recyclerViewContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId)).check(matches(hasDescendant(withText(itemName))))
    }

    fun recyclerViewNotContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId)).check(matches(not(hasDescendant(withText(itemName)))))
    }

    fun textHasFixedLines(@IdRes viewId: Int, numberOfLines: Int) {

        onView(withId(viewId))
            .check(matches(isTextInLines(numberOfLines)))
    }

    fun checkBoxIsChecked(@IdRes viewId: Int) {
        onView(withId(viewId)).check(matches(isChecked()))
    }

    private fun isTextInLines(lines: Int): Matcher<in View>? =
        object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                return (item as? TextView)?.lineCount == lines
            }

            override fun describeTo(description: Description?) {
                description?.appendText("check number of lines")
            }
        }
}
