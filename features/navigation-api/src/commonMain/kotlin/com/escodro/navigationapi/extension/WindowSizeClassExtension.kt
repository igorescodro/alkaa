package com.escodro.navigationapi.extension

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * Check if the [WindowSizeClass] is compact.
 *
 * @return true if the [WindowSizeClass] is compact, false otherwise
 */
fun WindowSizeClass.isCompact(): Boolean =
    windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
        windowHeightSizeClass == WindowHeightSizeClass.COMPACT
