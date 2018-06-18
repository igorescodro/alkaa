package com.escodro.alkaa.common.adapter

import android.databinding.BindingAdapter
import android.graphics.Color
import android.view.View

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
