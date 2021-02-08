package com.escodro.task.presentation.fake

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.task.LoadTask

internal class LoadTaskFake : LoadTask {

    var taskToBeReturned: Task? = null

    override suspend fun invoke(taskId: Long): Task? =
        taskToBeReturned
}
