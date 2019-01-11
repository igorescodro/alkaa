package com.escodro.alkaa.framework

import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider
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
import org.hamcrest.Matchers.not
import java.util.Calendar

/**
 * Handles all the test matchers.
 */
@Suppress("UndocumentedPublicFunction")
class Checkers {

    private val context: Context = ApplicationProvider.getApplicationContext()

    fun viewIsCompletelyDisplayed(@IdRes viewId: Int) {
        onView(withId(viewId)).check(matches(isCompletelyDisplayed()))
    }

    fun viewHasText(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).check(matches(withText(text)))
    }

    fun viewHasText(@IdRes toolbarId: Int, @StringRes resId: Int) {
        onView(withText(resId)).check(matches(withParent(withId(toolbarId))))
    }

    fun viewHasDate(@IdRes viewId: Int, calendar: Calendar) {
        onView(withId(viewId)).check(matches(Matchers.compareDates(calendar)))
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
            .check(matches(Matchers.isTextInLines(numberOfLines)))
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
}
