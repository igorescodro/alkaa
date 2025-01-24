package com.escodro.navigation.event

import com.escodro.navigation.destination.Destination
import com.escodro.navigation.destination.TasksDestination

object SearchEvent {

    data class OnTaskItemClick(val taskId: Long) : Event {
        override fun nextDestination(): Destination = TasksDestination.TaskDetail(taskId = taskId)
    }
}
