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
}

/**
 * Represents the arguments to be passed through the [Destinations].
 */
object DestinationArgs {

    /**
     * Argument to be passed to [Destinations.TaskDetail] representing the task id to be detailed.
     */
    const val TaskId = "task_id"
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
