package com.escodro.alkaa.common.extension

import android.app.Activity
import android.view.inputmethod.InputMethodManager

/**
 * Hides the opened soft keyboard from the [Activity].
 */
fun Activity.hideKeyboard() {
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}
