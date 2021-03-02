package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskTitle
import javax.inject.Inject

internal class UpdateTaskTitleImpl @Inject constructor(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask
) : UpdateTaskTitle {

    override suspend fun invoke(taskId: Long, title: String) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(title = title)
        updateTask(updatedTask)
    }
}
