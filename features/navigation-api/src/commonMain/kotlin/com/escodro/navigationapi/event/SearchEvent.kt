package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.TasksDestination

object SearchEvent {

    data class OnTaskItemClick(val taskId: Long) : Event {
        override fun nextDestination(): Destination = TasksDestination.TaskDetail(taskId = taskId)
    }
}
