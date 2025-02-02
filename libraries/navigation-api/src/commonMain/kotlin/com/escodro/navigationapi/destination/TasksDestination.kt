package com.escodro.navigationapi.destination

import com.escodro.navigationapi.marker.TopAppBarVisible
import kotlinx.serialization.Serializable

object TasksDestination {

    @Serializable
    data class TaskDetail(val taskId: Long) : Destination

    @Serializable
    data object AddTaskBottomSheet : Destination, TopAppBarVisible
}
