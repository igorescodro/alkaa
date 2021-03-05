package com.escodro.task.presentation.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.AddTask

internal class AddTaskFake : AddTask {

    private val updatedList: MutableList<Task> = mutableListOf()

    override suspend fun invoke(task: Task) {
        updatedList.add(task)
    }

    fun clear() =
        updatedList.clear()

    fun wasTaskCreated(title: String): Boolean =
        updatedList.any { it.title == title }
}
