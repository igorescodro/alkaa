package com.escodro.navigationapi.deeplink

import android.content.Intent
import androidx.core.net.toUri
import com.escodro.navigationapi.destination.Destination

/**
 * Destinations specifically for the Android platform since it requires [Intent] to navigate between
 * screens in some flows, such as notifications and widgets.
 */
object AndroidDeepLink {

    /**
     * Returns the [Intent] to the home screen.
     */
    fun homeIntent(): Intent = Intent(Intent.ACTION_VIEW, Destination.URI.toUri())

    /**
     * Returns the [Intent] to the task detail screen.
     *
     * @param taskId the task id to be shown
     */
    fun taskDetail(taskId: Long): Intent = homeIntent().apply {
        // TODO Add the task detail when CMP supports deep links
    }
}
