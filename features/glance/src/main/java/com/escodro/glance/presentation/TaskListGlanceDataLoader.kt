package com.escodro.glance.presentation

import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import com.escodro.glance.mapper.TaskMapper
import com.escodro.glance.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TaskListGlanceDataLoader(
    private val loadAllTasksUseCase: LoadUncompletedTasks,
    private val taskMapper: TaskMapper
) {

    fun loadTaskList(categoryId: Long? = null): Flow<List<Task>> =
        loadAllTasksUseCase(categoryId = categoryId).map { taskMapper.toView(it) }
}
