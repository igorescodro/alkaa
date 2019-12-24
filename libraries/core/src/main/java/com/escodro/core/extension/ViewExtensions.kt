package com.escodro.core.extension

import android.graphics.Color
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce

/**
 * Extension to check if the given drawer view is currently in an open state, abstracting the
 * default gravity.
 *
 * @param gravity gravity of the drawer to check
 *
 * @return `true` if the drawer is open, `false` otherwise
 */
fun DrawerLayout.isOpen(gravity: Int = GravityCompat.START) =
    isDrawerOpen(gravity)

/**
 * Extension to close the specified drawer view by animating it into view, abstracting the default
 * gravity.
 *
 * @param gravity [GravityCompat.START] to move the left drawer or [GravityCompat.END] for the
 * right.
 */
fun DrawerLayout.close(gravity: Int = GravityCompat.START) =
    closeDrawer(gravity)

/**
 * Extension to observe text change updates from a [TextView] with a debounce of
 * [TEXT_UPDATE_DEBOUNCE] milliseconds.
 *
 * @return a [Flow] with the updated text
 */
fun TextView.textChangedFlow(): Flow<String> {
    val flow: Flow<String> = callbackFlow {
        val listener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Do nothing.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do nothing.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                offer(s.toString())
            }
        }
        addTextChangedListener(listener)
        awaitClose { removeTextChangedListener(listener) }
    }

    return flow.debounce(TEXT_UPDATE_DEBOUNCE)
}

/**
 * Extension to get the [EditText] string text when the user presses the
 * [EditorInfo.IME_ACTION_DONE] in the keyboard.
 *
 * @param onActionDone HFO to be executed with the input text
 */
fun EditText.onActionDone(onActionDone: (String) -> Unit) {
    setOnEditorActionListener { textView, action, _ ->
        val isActionClicked = action == EditorInfo.IME_ACTION_DONE
        if (isActionClicked) {
            onActionDone(textView.stringText())
            textView.text = ""
            textView.clearFocus()
        }
        isActionClicked
    }
}

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
