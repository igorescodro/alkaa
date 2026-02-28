package com.escodro.domain.usecase.task.implementation

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.usecase.task.LoadTaskFlow
import kotlinx.coroutines.flow.Flow

internal class LoadTaskFlowImpl(private val taskRepository: TaskRepository) : LoadTaskFlow {

    override operator fun invoke(taskId: Long): Flow<Task?> =
        taskRepository.findTaskByIdFlow(taskId)
}
