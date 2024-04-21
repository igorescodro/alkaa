package com.escodro.task.presentation.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask

internal class AddTaskFake : AddTask {

    var createdTask: Task? = null

    override suspend fun invoke(task: Task) {
        createdTask = task
    }

    fun clear() {
        createdTask = null
    }
}
