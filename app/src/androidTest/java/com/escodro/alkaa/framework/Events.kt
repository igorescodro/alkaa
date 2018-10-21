package com.escodro.alkaa.framework

import android.content.Context
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import org.hamcrest.Matcher
import java.util.Calendar

/**
 * Handles all the test events.
 */
@Suppress("UndocumentedPublicFunction")
class Events {

    private val context: Context = ApplicationProvider.getApplicationContext()

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

    fun clickOnRadioButton(@IdRes radioButtonGroupId: Int, index: Int) {
        onView(Matchers.getChildAt(withId(radioButtonGroupId), index)).perform(click())
    }

    fun textOnView(@IdRes viewId: Int, text: String) {
        onView(withId(viewId)).perform(replaceText(text))
    }

    fun pressImeActionButton(@IdRes viewId: Int) {
        onView(withId(viewId)).perform(pressImeActionButton())
    }

    fun navigateUp() {
        onView(withContentDescription("Navigate up")).perform(click())
    }

    fun waitFor(@IdRes viewId: Int, delay: Long) {
        onView(isRoot()).perform(waitId(viewId, delay))
    }

    fun openDrawer(@IdRes drawerId: Int) {
        onView(withId(drawerId)).perform(DrawerActions.open())
    }

    fun clickOnNavigationViewItem(@IdRes viewId: Int, itemId: Int) {
        onView(withId(viewId)).perform(NavigationViewActions.navigateTo(itemId))
    }

    fun setDate(calendar: Calendar) {
        onView(withClassName(org.hamcrest.Matchers.equalTo(DatePicker::class.java.name)))
            .perform(
                PickerActions.setDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
            )
    }

    fun setTime(calendar: Calendar) {
        onView(withClassName(org.hamcrest.Matchers.equalTo(TimePicker::class.java.name)))
            .perform(
                PickerActions.setTime(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE)
                )
            )
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
