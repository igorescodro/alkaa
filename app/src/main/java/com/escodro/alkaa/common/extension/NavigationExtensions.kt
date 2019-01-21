package com.escodro.alkaa.common.extension

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * Extension to navigate to a destination, clearing the current back stack and launching the
 * fragment as `Single Top`, from the current navigation graph. This supports both navigating via an
 * {@link NavDestination#getAction(int) action} and directly navigating to a destination.
 *
 * @param resId an [androidx.navigation.NavDestination.getAction] id or a destination id to
 *              navigate to
 * @param args arguments to pass to the destination
 */
fun NavController.navigateSingleTop(@IdRes resId: Int, args: Bundle? = null) {
    val hostDestinationId = graph.startDestination
    val navOptions = NavOptions.Builder()
        .setPopUpTo(hostDestinationId, false)
        .setLaunchSingleTop(true)
        .build()
    navigate(resId, args, navOptions)
}
