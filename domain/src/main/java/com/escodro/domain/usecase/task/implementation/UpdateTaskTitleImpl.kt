package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.domain.usecase.task.LoadTask
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.domain.usecase.task.UpdateTaskTitle

internal class UpdateTaskTitleImpl(
    private val loadTask: LoadTask,
    private val updateTask: UpdateTask,
    private val glanceInteractor: GlanceInteractor
) : UpdateTaskTitle {

    override suspend fun invoke(taskId: Long, title: String) {
        val task = loadTask(taskId) ?: return
        val updatedTask = task.copy(title = title)
        updateTask(updatedTask)
        glanceInteractor.onTaskListUpdated()
    }
}
