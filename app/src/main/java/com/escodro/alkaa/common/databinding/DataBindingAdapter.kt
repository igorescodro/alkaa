package com.escodro.alkaa.common.databinding

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.escodro.alkaa.common.extension.format
import java.util.Calendar

/**
 * Sets a color in [String] format as [View] background.
 *
 * @param view view to have the background updated
 * @param color color in String format (#XXXXXX)
 */
@BindingAdapter("android:background")
fun setViewBackgroundColor(view: View, color: String?) {
    if (color == null) {
        return
    }

    val backgroundColor = Color.parseColor(color)
    view.setBackgroundColor(backgroundColor)
}

/**
 * Formats and sets a [Calendar] in a [TextView].
 *
 * @param view text view to be updated
 * @param calendar calendar to be formatted and set
 */
@BindingAdapter("android:text")
fun setFormattedCalendar(view: TextView, calendar: Calendar?) {
    if (calendar == null) {
        return
    }

    view.text = calendar.format()
}

/**
 * Sets the visibility based on [Boolean] value. If the value is `true`, it is set to [View
 * .VISIBLE], otherwise is set to [View.INVISIBLE].
 *
 * @param view text view to be updated
 * @param isVisible boolean to represent if the view should be visible
 */
@BindingAdapter("android:visibility")
fun setVisibility(view: View, isVisible: Boolean?) {
    if (isVisible == null) {
        return
    }

    val visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
    view.visibility = visibility
}
