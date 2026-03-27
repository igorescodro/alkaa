package com.escodro.domain.model

sealed class TaskGroup {
    abstract val tasks: List<Task>

    data class Overdue(override val tasks: List<Task>) : TaskGroup()
    data class DueToday(override val tasks: List<Task>) : TaskGroup()
    data class Upcoming(override val tasks: List<Task>) : TaskGroup()
    data class NoDueDate(override val tasks: List<Task>) : TaskGroup()
    data class Completed(override val tasks: List<Task>) : TaskGroup()
}
