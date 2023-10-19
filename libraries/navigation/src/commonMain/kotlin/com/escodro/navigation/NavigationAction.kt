package com.escodro.navigation

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize

/**
 * Navigation action to be executed using deep links.
 */
sealed interface NavigationAction : Parcelable {

    /**
     * Home screen navigation action.
     */
    @Parcelize
    data object Home : NavigationAction

    /**
     * Task detail screen navigation action.
     *
     * @param taskId the task id to be shown
     */
    @Parcelize
    data class TaskDetail(val taskId: Long) : NavigationAction

    companion object {

        /**
         * Key to be used to retrieve the [NavigationAction] from the platform-specific map.
         */
        const val EXTRA_DESTINATION = "destination"
    }
}
