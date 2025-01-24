package com.escodro.navigation.event

import com.escodro.navigation.destination.Destination
import com.escodro.navigation.destination.TasksDestination

object TaskEvent {

    data object OnNewTaskClick : Event {
        override fun nextDestination(): Destination = TasksDestination.AddTaskBottomSheet
    }

    data class OnTaskClick(val id: Long) : Event {
        override fun nextDestination(): Destination = TasksDestination.TaskDetail(taskId = id)
    }
}
