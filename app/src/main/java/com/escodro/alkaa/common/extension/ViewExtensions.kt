package com.escodro.alkaa.common.extension

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

/**
 * Extension to check if the given drawer view is currently in an open state, abstracting the
 * default gravity.
 *
 * @param gravity gravity of the drawer to check
 *
 * @return `true` if the drawer is open, `false` otherwise
 */
fun DrawerLayout.isOpen(gravity: Int = GravityCompat.START) =
    isDrawerOpen(gravity)

/**
 * Extension to close the specified drawer view by animating it into view, abstracting the default
 * gravity.
 *
 * @param gravity [GravityCompat.START] to move the left drawer or [GravityCompat.END] for the
 * right.
 */
fun DrawerLayout.close(gravity: Int = GravityCompat.START) =
    closeDrawer(gravity)
