package com.escodro.designsystem.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Converts a hex color string to ARGB color.
 *
 * @return the ARGB color
 */
fun String.toArgbColor(): Int =
    Color(removePrefix("#").toLong(16) or 0x00000000FF000000).toArgb()
