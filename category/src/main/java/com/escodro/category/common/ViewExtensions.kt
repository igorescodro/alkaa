package com.escodro.category.common

import android.graphics.Color
import android.widget.CompoundButton
import android.widget.RadioButton

/**
 * Get the Tint Color from the [RadioButton].
 *
 * @return the Tint Color from the [RadioButton]
 */
fun CompoundButton.getTintColor(): Int {
    val intColor = buttonTintList?.defaultColor ?: return Color.WHITE
    return intColor.let { Color.parseColor(intColor.toStringColor()) }
}
