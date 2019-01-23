package com.escodro.alkaa.common.extension

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.snackbar.Snackbar
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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
 * @return a thread safe [Observable] with the updated text
 */
fun TextView.textChangedObservable(): Observable<String> {
    val textObservable = PublishSubject.create<String>()
    val listener = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            // Do nothing.
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing.
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            textObservable.onNext(s.toString())
        }
    }

    addTextChangedListener(listener)

    return textObservable
        .debounce(TEXT_UPDATE_DEBOUNCE, TimeUnit.MILLISECONDS)
        .distinctUntilChanged()
        .applySchedulers()
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
fun RadioButton.getTintColor(): Int {
    val intColor = buttonTintList?.defaultColor ?: return Color.WHITE
    return intColor.let { Color.parseColor(intColor.toStringColor()) }
}

/**
 * Shows a [Snackbar] with the given message.
 *
 * @param messageId the message String resource id
 * @param duration the Snackbar duration, if not provided will be set to [Snackbar.LENGTH_LONG]
 */
fun View.showSnackbar(@StringRes messageId: Int, duration: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, messageId, duration).show()
}

private fun TextView.stringText() = text.toString()

private const val TEXT_UPDATE_DEBOUNCE = 500L
