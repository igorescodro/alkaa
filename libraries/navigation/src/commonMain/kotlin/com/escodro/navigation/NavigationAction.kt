package com.escodro.navigation

import com.escodro.parcelable.CommonParcelable
import com.escodro.parcelable.CommonParcelize

/**
 * Navigation action to be executed using deep links.
 */
sealed interface NavigationAction : CommonParcelable {

    /**
     * Home screen navigation action.
     */
    @CommonParcelize
    data object Home : NavigationAction

    /**
     * Task detail screen navigation action.
     *
     * @param taskId the task id to be shown
     */
    @CommonParcelize
    data class TaskDetail(val taskId: Long) : NavigationAction

    companion object {

        /**
         * Key to be used to retrieve the [NavigationAction] from the platform-specific map.
         */
        const val EXTRA_DESTINATION = "destination"
    }
}
