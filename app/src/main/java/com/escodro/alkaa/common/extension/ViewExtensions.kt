package com.escodro.alkaa.common.extension

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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

private fun TextView.stringText() = text.toString()

private const val TEXT_UPDATE_DEBOUNCE = 500L
