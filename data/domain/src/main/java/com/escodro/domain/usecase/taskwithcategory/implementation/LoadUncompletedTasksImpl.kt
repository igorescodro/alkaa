package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LoadUncompletedTasksImpl @Inject constructor(
    private val repository: TaskWithCategoryRepository
) : LoadUncompletedTasks {

    override operator fun invoke(): Flow<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory()
            .map { list -> list.filterNot { item -> item.task.completed } }
}
