package com.escodro.glance.data

import com.escodro.domain.usecase.task.UpdateTaskStatus
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Class responsible to provide data and update the Task List in the Glance Widget.
 */
internal class TaskListGlanceUpdater(
    private val loadAllTasksUseCase: LoadUncompletedTasks,
    private val updateTaskStatus: UpdateTaskStatus,
    private val taskMapper: TaskMapper,
) {

    fun loadTaskList(categoryId: Long? = null): Flow<List<Task>> =
        loadAllTasksUseCase(categoryId = categoryId).map { taskMapper.toView(it) }

    suspend fun updateTaskAsCompleted(taskId: Long) {
        updateTaskStatus(taskId)
    }
}
