package com.escodro.navigationapi.extension

import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * Check if the [WindowSizeClass] has a compact width, meaning that the layout should be displayed
 * in a single pane.
 *
 * @return true if the [WindowSizeClass] is single pane, false otherwise.
 */
fun WindowSizeClass.isSinglePane(): Boolean =
    windowWidthSizeClass == WindowWidthSizeClass.COMPACT ||
        windowHeightSizeClass == WindowHeightSizeClass.COMPACT
