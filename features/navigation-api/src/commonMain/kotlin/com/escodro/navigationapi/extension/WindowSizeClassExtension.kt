package com.escodro.navigationapi.extension

import androidx.window.core.layout.WindowSizeClass

/**
 * Check if the [WindowSizeClass] has a compact width, meaning that the layout should be displayed
 * in a single pane.
 *
 * @return true if the [WindowSizeClass] is single pane, false otherwise.
 */
fun WindowSizeClass.isSinglePane(): Boolean =
    !isWidthAtLeastBreakpoint(widthDpBreakpoint = WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
