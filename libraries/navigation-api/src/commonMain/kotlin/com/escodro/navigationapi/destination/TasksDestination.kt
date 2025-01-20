package com.escodro.navigationapi.destination

import kotlinx.serialization.Serializable

object TasksDestination {

    @Serializable
    data object Home : Destination

    @Serializable
    data class TaskDetail(val taskId: Long) : Destination

    @Serializable
    data object AddTaskBottomSheet : Destination
}
