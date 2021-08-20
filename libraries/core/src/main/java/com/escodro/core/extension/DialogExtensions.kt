package com.escodro.core.extension

import android.content.Context
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

/**
 * Add items in the [AlertDialog].
 *
 * @param resArray array with the items
 * @param func HFO called when a item is clicked
 */
fun AlertDialog.Builder.items(
    @ArrayRes resArray: Int,
    func: AlertDialog.Builder.(item: Int) -> Unit
) {
    setItems(resArray) { _, item -> func(item) }
}

/**
 * Create a custom [AlertDialog].
 *
 * @param titleRes dialog title
 * @param message dialog description message
 * @param builder HFO to build dialog with custom parameters
 *
 * @return an instance of [AlertDialog]
 */
fun Context.dialog(
    @StringRes titleRes: Int,
    @StringRes message: Int? = null,
    builder: AlertDialog.Builder.() -> Unit
): AlertDialog.Builder =
    AlertDialog.Builder(this).apply {
        setTitle(titleRes)
        message?.let { setMessage(it) }
        builder()
    }

/**
 * Action to be taken in the positive button.
 *
 * @param resTitle title of positive button
 * @param func HFO called when positive button is clicked
 *
 * @return the [AlertDialog] itself
 */
fun AlertDialog.Builder.positiveButton(
    @StringRes resTitle: Int,
    func: AlertDialog.Builder.() -> Unit
) {
    setPositiveButton(resTitle) { _: DialogInterface?, _: Int -> func() }
}

/***
 * Action to be taken in the negative button.
 *
 * @param resTitle title of negative button
 * @param func HFO called when negative button is clicked
 *
 * @return the [AlertDialog] itself
 */
fun AlertDialog.Builder.negativeButton(
    @StringRes resTitle: Int,
    func: AlertDialog.Builder.() -> Unit
) {
    setNegativeButton(resTitle) { _: DialogInterface?, _: Int -> func() }
}

/**
 * Sets the dialog view.
 *
 * @param viewRes the layout resource
 */
fun AlertDialog.Builder.view(@LayoutRes viewRes: Int) {
    setView(viewRes)
}
