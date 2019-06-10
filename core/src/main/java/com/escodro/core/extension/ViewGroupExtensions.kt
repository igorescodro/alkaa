package com.escodro.core.extension

import android.view.View
import android.view.ViewGroup

/**
 * Gets the [List] of all the child inside the [ViewGroup].
 *
 * @return list of all the child inside the [ViewGroup].
 */
@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.getChildren(): List<T> {
    val size = this.childCount

    val list = mutableListOf<T>()
    for (i in 0..size) {
        val view = this.getChildAt(i) as? T
        view?.let { list.add(it) }
    }

    return list
}
