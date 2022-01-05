package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.AddTask
import mu.KLogging

internal class AddTaskImpl(
    private val taskRepository: TaskRepository,
    private val glanceInteractor: GlanceInteractor
) : AddTask {

    override suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            logger.debug { "Task cannot be inserted with a empty title" }
            return
        }

        taskRepository.insertTask(task)
        glanceInteractor.onTaskListUpdated()
    }

    companion object : KLogging()
}
