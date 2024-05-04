package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.interactor.GlanceInteractor
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.alarm.UpdateAlarm
import com.escodro.domain.usecase.task.AddTask
import mu.KotlinLogging

internal class AddTaskImpl(
    private val taskRepository: TaskRepository,
    private val updateAlarm: UpdateAlarm,
    private val glanceInteractor: GlanceInteractor?,
) : AddTask {

    private val logger = KotlinLogging.logger {}

    override suspend operator fun invoke(task: Task) {
        if (task.title.isBlank()) {
            logger.debug { "Task cannot be inserted with a empty title" }
            return
        }

        val taskId = taskRepository.insertTask(task)
        val insertedTask = taskRepository.findTaskById(taskId)

        if (insertedTask?.dueDate != null) {
            logger.debug { "Adding alarm for task ${task.id}" }
            updateAlarm(insertedTask)
        }

        glanceInteractor?.onTaskListUpdated()
    }
}
