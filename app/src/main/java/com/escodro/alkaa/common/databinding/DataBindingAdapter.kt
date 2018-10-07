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
@BindingAdapter("background")
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
