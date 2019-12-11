package com.escodro.core.databinding

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.escodro.core.extension.format
import java.util.Calendar

/**
 * Formats and sets a [Calendar] in a [TextView].
 *
 * @param view text view to be updated
 * @param calendar calendar to be formatted and set
 */
@BindingAdapter("android:text")
fun setFormattedCalendar(view: TextView, calendar: Calendar?) {
    view.text = calendar?.format()
}

/**
 * Sets the visibility based on [Boolean] value. If the value is `true`, it is set to [View
 * .VISIBLE], otherwise is set to [View.GONE].
 *
 * @param view text view to be updated
 * @param isVisible boolean to represent if the view should be visible
 */
@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean?) {
    if (isVisible == null) {
        return
    }

    val visibility = if (isVisible) View.VISIBLE else View.GONE
    view.visibility = visibility
}
