package com.escodro.alkaa.framework

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.matcher.ViewMatchers.hasErrorText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matchers.not

/**
 * Handles all the test matchers.
 */
@Suppress("UndocumentedPublicFunction")
class Matchers {

    private val context = InstrumentationRegistry.getTargetContext()

    fun viewContainsError(@IdRes viewId: Int, @StringRes stringResource: Int) {
        val errorMessage = context.getString(stringResource)
        onView(withId(viewId)).check(matches(hasErrorText(errorMessage)))
    }

    fun recyclerViewContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId))
            .check(matches(hasDescendant(withText(itemName))))
    }

    fun recyclerViewNotContainsItem(@IdRes viewId: Int, itemName: String) {
        onView(withId(viewId))
            .check(matches(not(hasDescendant(withText(itemName)))))
    }
}
