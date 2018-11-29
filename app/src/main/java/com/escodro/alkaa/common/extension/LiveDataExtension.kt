package com.escodro.alkaa.common.extension

import androidx.lifecycle.MutableLiveData

/**
 * Notifies the [MutableLiveData] that it needs to be reloaded.
 */
fun <T> MutableLiveData<T>.notify() {
    value = value
}
