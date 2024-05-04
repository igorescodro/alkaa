package com.escodro.task.presentation.factory

import com.escodro.domain.model.Task
import kotlinx.datetime.LocalDateTime

internal object TaskFactory {

    fun createTask(
        id: Long,
        dueDate: LocalDateTime?,
    ): Task = Task(id = id, title = "Task Title", dueDate = dueDate)
}
