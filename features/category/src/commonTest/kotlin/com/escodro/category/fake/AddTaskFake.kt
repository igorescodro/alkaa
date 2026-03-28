package com.escodro.category.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask

internal class AddTaskFake : AddTask {
    val addedTasks = mutableListOf<Task>()

    override suspend fun invoke(task: Task) {
        addedTasks.add(task)
    }

    fun clear() {
        addedTasks.clear()
    }
}
