package com.escodro.glance.presentation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import kotlinx.coroutines.flow.Flow

internal class TaskListGlanceDataLoader(private val loadAllTasksUseCase: LoadUncompletedTasks) {

    fun loadTaskList(categoryId: Long? = null): Flow<List<TaskWithCategory>> =
        loadAllTasksUseCase(categoryId = categoryId)
}
