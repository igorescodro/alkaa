package com.escodro.alkaa.framework

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.hasErrorText
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.containsString
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

    fun viewHasText(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).check(matches(withText(text)))
    }

    fun viewContainsText(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).check(matches(withText(containsString(text))))
    }

    fun toolbarContainsTitle(@IdRes toolbarId: Int, @StringRes resId: Int) {
        onView(withText(resId)).check(matches(withParent(withId(toolbarId))))
    }

    fun toolbarContainsTitle(@IdRes toolbarId: Int, text: String) {
        onView(withText(text)).check(matches(withParent(withId(toolbarId))))
    }

    fun viewContainsError(@IdRes viewId: Int, @StringRes stringResource: Int) {
        val errorMessage = context.getString(stringResource)
        onView(withId(viewId)).check(matches(hasErrorText(errorMessage)))
    }

    fun listContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId)).check(matches(hasDescendant(withText(itemName))))
    }

    fun listNotContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId)).check(matches(not(hasDescendant(withText(itemName)))))
    }

    fun textHasFixedLines(@IdRes viewId: Int, numberOfLines: Int) {
        onView(withId(viewId))
            .check(matches(isTextInLines(numberOfLines)))
    }

    fun checkBoxIsChecked(@IdRes viewId: Int) {
        onView(withId(viewId)).check(matches(isChecked()))
    }

    fun radioButtonIsChecked(@IdRes radioButtonGroupId: Int, index: Int) {
        onView(Matchers.getChildAt(withId(radioButtonGroupId), index)).check(matches(isChecked()))
    }

    fun drawerIsOpen(@IdRes drawerId: Int) {
        onView(withId(drawerId)).check(matches(DrawerMatchers.isOpen()))
    }

    fun drawerIsClosed(@IdRes drawerId: Int) {
        onView(withId(drawerId)).check(matches(DrawerMatchers.isClosed()))
    }

    companion object {

        private fun isTextInLines(lines: Int): Matcher<in View>? =
            object : TypeSafeMatcher<View>() {
                override fun matchesSafely(item: View?): Boolean {
                    return (item as? TextView)?.lineCount == lines
                }

                override fun describeTo(description: Description?) {
                    description?.appendText("check number of lines")
                }
            }

        fun getChildAt(parentMatcher: Matcher<View>, index: Int) =
            object : TypeSafeMatcher<View>() {
                override fun matchesSafely(view: View?): Boolean {
                    if (view?.parent !is ViewGroup) {
                        return parentMatcher.matches(view?.parent)
                    }

                    val viewGroup = view.parent as ViewGroup
                    return parentMatcher.matches(view.parent) && viewGroup.getChildAt(index) == view
                }

                override fun describeTo(description: Description?) {
                    description?.appendText("click at child $index from ViewGroup")
                }
            }
    }
}
