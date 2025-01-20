package com.escodro.navigation.deeplink

import android.content.Intent
import android.net.Uri
import com.escodro.navigationapi.destination.Destination

/**
 * Destinations specifically for the Android platform since it requires [Intent] to navigate between
 * screens in some flows, such as notifications and widgets.
 */
object AndroidDeepLink {

    /**
     * Returns the [Intent] to the home screen.
     */
    fun homeIntent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse(Destination.URI))

    /**
     * Returns the [Intent] to the task detail screen.
     *
     * @param taskId the task id to be shown
     */
    fun taskDetail(taskId: Long): Intent = homeIntent().apply {
        // TODO Add the task detail when CMP supports deep links
    }
}
