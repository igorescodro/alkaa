package com.escodro.core.extension

import android.database.Cursor

/**
 * Gets the [Int] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getIntFromColumn(columnName: String): Int? =
    this.getInt(this.getColumnIndex(columnName))

/**
 * Gets the [String] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getStringFromColumn(columnName: String): String? =
    this.getString(this.getColumnIndex(columnName))

/**
 * Gets the [Long] value from the [Cursor] from the column name.
 *
 * @param columnName the column to get the cursor value
 */
fun Cursor.getLongFromColumn(columnName: String): Long? =
    this.getLong(this.getColumnIndex(columnName))
