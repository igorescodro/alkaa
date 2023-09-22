package com.escodro.navigation

import cafe.adriel.voyager.core.registry.ScreenProvider

/**
 * Destinations available in the app.
 */
object AlkaaDestinations {

    /**
     * Home destinations.
     */
    sealed interface Task : ScreenProvider {

        /**
         * Task details destination, showing all the information about a task.
         */
        data class TaskDetail(val taskId: Long) : ScreenProvider

        /**
         * Task bottom sheet destination, allowing users adding new tasks.
         */
        data object AddTaskBottomSheet : ScreenProvider
    }

    /**
     * Category destinations.
     */
    sealed interface Category : ScreenProvider {

        /**
         * Category bottom sheet destination, allowing users adding and editing categories.
         */
        data class CategoryBottomSheet(val categoryId: Long?) : ScreenProvider
    }
}
