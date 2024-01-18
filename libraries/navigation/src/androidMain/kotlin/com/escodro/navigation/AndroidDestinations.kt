package com.escodro.navigation

import android.content.Intent
import android.net.Uri

/**
 * Destinations specifically for the Android platform since it requires [Intent] to navigate between
 * screens in some flows, such as notifications and widgets.
 */
object AndroidDestinations {

    /**
     * Returns the [Intent] to the home screen.
     */
    fun homeIntent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(HOME_DEEP_LINK))

    /**
     * Returns the [Intent] to the task detail screen.
     *
     * @param taskId the task id to be shown
     */
    fun taskDetail(taskId: Long): Intent = homeIntent().apply {
        val action = NavigationAction.TaskDetail(taskId)
        putExtra(NavigationAction.EXTRA_DESTINATION, action)
    }

    /**
     * Main activity's deep link.
     */
    private const val HOME_DEEP_LINK = "app://com.escodro.alkaa"
}
