package com.escodro.navigation

import android.net.Uri
import androidx.core.net.toUri

/**
 * Represents the possible destinations of Alkaa via Compose Navigation. The destinations represents
 * a flow where a backstack is required, so the tabs inside the Home destination is a simple
 * navigation.
 */
object Destinations {

    /**
     * Home destination.
     */
    const val Home = "home"

    /**
     * Task detail destination.
     */
    const val TaskDetail = "task_detail"

    /**
     * About destination.
     */
    const val About = "about"

    /**
     * Task Tracker dynamic feature destination.
     */
    const val Tracker = "tracker"

    /**
     * Represents the navigation where the target is a BottomSheet rather than a full screen.
     */
    object BottomSheet {

        /**
         * Bottom Sheet Category destination.
         */
        const val Category = "bottom_sheet_category"

        /**
         * Bottom Sheet Category destination.
         */
        const val Task = "bottom_sheet_task"
    }
}

/**
 * Represents the arguments to be passed through the [Destinations].
 */
object DestinationArgs {

    /**
     * Argument to be passed to [Destinations.TaskDetail] representing the task id to be detailed.
     */
    const val TaskId = "task_id"

    /**
     * Argument to be passed to [Destinations.BottomSheet.Category] representing the category id to
     * be detailed.
     */
    const val CategoryId = "category_id"
}

/**
 * Represents the Deep Links to implicit navigate through the application, like PendingIntent.
 */
object DestinationDeepLink {

    private val BaseUri = "app://com.escodro.alkaa".toUri()

    /**
     * Deep link pattern to be registered in [Destinations.Home] composable.
     */
    val HomePattern = "$BaseUri/home"

    /**
     * Deep link pattern to be registered in [Destinations.TaskDetail] composable.
     */
    val TaskDetailPattern = "$BaseUri/${DestinationArgs.TaskId}={${DestinationArgs.TaskId}}"

    /**
     * Deep link pattern to be registered in [Destinations.BottomSheet.Category] composable.
     */
    val CategorySheetPattern =
        "$BaseUri/${DestinationArgs.CategoryId}={${DestinationArgs.CategoryId}}"

    /**
     * Returns the [Destinations.TaskDetail] deep link with the argument set.
     *
     * @return the [Destinations.TaskDetail] deep link with the argument set
     */
    fun getTaskDetailUri(taskId: Long): Uri =
        "$BaseUri/${DestinationArgs.TaskId}=$taskId".toUri()

    /**
     * Returns the [Destinations.Home] deep link with the argument set.
     *
     * @return the [Destinations.Home] deep link with the argument set
     */
    fun getTaskHomeUri(): Uri =
        HomePattern.toUri()
}
