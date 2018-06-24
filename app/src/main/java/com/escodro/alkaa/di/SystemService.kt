package com.escodro.alkaa.di

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

/**
 * Provides the Android system services.
 */
class SystemService(private val context: Context) {

    /**
     * Gets the [Activity.INPUT_METHOD_SERVICE] service.
     *
     * @return the [Activity.INPUT_METHOD_SERVICE] service
     */
    fun getInputMethodManager() =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
}
