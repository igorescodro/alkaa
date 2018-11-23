package com.escodro.alkaa.common.extension

import android.app.AlertDialog
import androidx.annotation.ArrayRes
import androidx.fragment.app.Fragment
import com.escodro.alkaa.common.view.DateTimePickerDialog
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
): AlertDialog.Builder =
    AlertDialog.Builder(context).apply {
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
