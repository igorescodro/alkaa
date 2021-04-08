package com.escodro.domain.usecase.taskwithcategory.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.taskwithcategory.LoadUncompletedTasks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class LoadUncompletedTasksImpl(
    private val repository: TaskWithCategoryRepository
) : LoadUncompletedTasks {

    override fun invoke(categoryId: Long?): Flow<List<TaskWithCategory>> =
        if (categoryId == null) {
            repository.findAllTasksWithCategory()
                .map { list -> list.filterNot { item -> item.task.completed } }
        } else {
            repository.findAllTasksWithCategoryId(categoryId)
        }.map { list -> list.filterNot { item -> item.task.completed } }
}
