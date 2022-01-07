package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.CompleteTask
import com.escodro.domain.usecase.task.UncompleteTask
import com.escodro.domain.usecase.task.UpdateTaskStatus

internal class UpdateTaskStatusImpl(
    private val taskRepository: TaskRepository,
    private val glanceInteractor: GlanceInteractor,
    private val completeTask: CompleteTask,
    private val uncompleteTask: UncompleteTask
) : UpdateTaskStatus {

    override suspend operator fun invoke(taskId: Long) {
        val task = taskRepository.findTaskById(taskId) ?: return
        when (task.completed.not()) {
            true -> completeTask(task)
            false -> uncompleteTask(task)
        }
        glanceInteractor.onTaskListUpdated()
    }
}
