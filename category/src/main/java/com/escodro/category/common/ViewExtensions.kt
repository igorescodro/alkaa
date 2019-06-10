package com.escodro.category.common

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

/**
 * Get the Tint Color from the [RadioButton].
 *
 * @return the Tint Color from the [RadioButton]
 */
fun CompoundButton.getTintColor(): Int {
    val intColor = buttonTintList?.defaultColor ?: return Color.WHITE
    return intColor.let { Color.parseColor(intColor.toStringColor()) }
}

/**
 * Creates a [Snackbar] with the given message.
 *
 * @param messageId the message String resource id
 * @param duration the Snackbar duration, if not provided will be set to [Snackbar.LENGTH_LONG]
 */
fun View.createSnackbar(@StringRes messageId: Int, duration: Int = Snackbar.LENGTH_LONG) =
    Snackbar.make(this, messageId, duration)

/**
 * Sets the [TextView] as strikethrough and disabled style.
 */
fun TextView.setStyleDisable() {
    paintFlags = this.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    isEnabled = false
}

private fun TextView.stringText() = text.toString()

private const val TEXT_UPDATE_DEBOUNCE = 500L
