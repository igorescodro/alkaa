package com.escodro.core.extension

/**
 * Converts the color in [Int] to [String] format.
 *
 * @return the color in String format
 */
fun Int.toStringColor() =
    String.format(HexFormat, HexWhite and this)

private const val HexFormat = "#%06X"

private const val HexWhite = 0xFFFFFF
