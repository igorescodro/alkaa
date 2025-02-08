package com.escodro.navigationapi.event

import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.TasksDestination

object TaskEvent {

    data object OnNewTaskClick : Event {
        override fun nextDestination(): Destination = TasksDestination.AddTaskBottomSheet
    }

    data class OnTaskClick(val id: Long) : Event {
        override fun nextDestination(): Destination = TasksDestination.TaskDetail(taskId = id)
    }
}
