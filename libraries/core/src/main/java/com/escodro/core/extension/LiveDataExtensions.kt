package com.escodro.core.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 * Observes only a single event from Live Data.
 *
 * @param lifecycleOwner the LifecycleOwner which controls the observer
 * @param observer the observer that will receive the events
 */
fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(
        lifecycleOwner,
        object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        }
    )
}
