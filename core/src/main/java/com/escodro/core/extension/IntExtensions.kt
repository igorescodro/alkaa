package com.escodro.core.extension

/**
 * Converts the color in [Int] to [String] format.
 *
 * @return the color in String format
 */
fun Int.toStringColor() =
    String.format(HEX_FORMAT, HEX_WHITE and this)

private const val HEX_FORMAT = "#%06X"

private const val HEX_WHITE = 0xFFFFFF
