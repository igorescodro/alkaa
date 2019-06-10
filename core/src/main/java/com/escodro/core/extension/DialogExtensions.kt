package com.escodro.core.extension

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.annotation.ArrayRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.escodro.core.view.DateTimePickerDialog
import java.util.Calendar

/**
 * Shows a [DateTimePickerDialog], abstracting its construction.
 *
 * @param onDateChanged HOF to receive the user input in [Calendar] format
 */
fun Fragment.showDateTimePicker(onDateChanged: (Calendar) -> Unit) {
    context?.let { context ->
        DateTimePickerDialog(context, onDateChanged).show()
    }
}

/**
 * Create an [AlertDialog].
 *
 * @param title alert title
 * @param builder expression to setup the itemDialog
 *
 * @return an instance of [AlertDialog]
 */
fun Fragment.itemDialog(
    title: String,
    builder: AlertDialog.Builder.() -> Unit
): AlertDialog.Builder = AlertDialog.Builder(context).apply {
    setTitle(title)
    builder()
}

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
fun Fragment.dialog(
    @StringRes titleRes: Int,
    message: String,
    builder: AlertDialog.Builder.() -> Unit
): AlertDialog.Builder =
    AlertDialog.Builder(context).apply {
        setTitle(titleRes)
        setMessage(message)
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
