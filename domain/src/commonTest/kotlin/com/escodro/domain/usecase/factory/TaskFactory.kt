package com.escodro.domain.usecase.factory

import com.escodro.domain.model.Task

internal object TaskFactory {

    fun createTask(): Task = Task(id = 123L, title = "Task Title")
}
