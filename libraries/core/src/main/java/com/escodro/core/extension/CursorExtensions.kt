package com.escodro.core.extension

import android.database.Cursor
import androidx.core.database.getIntOrNull
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull

/**
 * Gets the [Int] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getIntFromColumn(columnName: String): Int? =
    this.getIntOrNull(this.getColumnIndex(columnName))

/**
 * Gets the [String] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getStringFromColumn(columnName: String): String? =
    this.getStringOrNull(this.getColumnIndex(columnName))

/**
 * Gets the [Long] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getLongFromColumn(columnName: String): Long? =
    this.getLongOrNull(this.getColumnIndex(columnName))
