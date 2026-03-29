package com.escodro.domain.usecase.fake

import com.escodro.domain.model.Category
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class TaskWithCategoryRepositoryFake(
    private val taskRepository: TaskRepositoryFake? = null,
    private val categoryRepository: CategoryRepositoryFake? = null,
) : TaskWithCategoryRepository {

    private val taskWithCategoryList = MutableStateFlow<List<TaskWithCategory>>(emptyList())

    fun insertTaskWithCategory(taskWithCategory: TaskWithCategory) {
        taskWithCategoryList.value = taskWithCategoryList.value + taskWithCategory
    }

    fun clear() {
        taskWithCategoryList.value = emptyList()
    }

    override fun findAllTasksWithCategory(): Flow<List<TaskWithCategory>> {
        if (taskRepository != null && categoryRepository != null) {
            return flow {
                val tasks = taskRepository.findAllTasks()
                    .map { task -> TaskWithCategory(task, findCategory(task.categoryId)) }
                emit(tasks)
            }
        }
        return taskWithCategoryList
    }

    override fun findAllTasksWithCategoryId(categoryId: Long): Flow<List<TaskWithCategory>> {
        if (taskRepository != null && categoryRepository != null) {
            return flow {
                val tasks = taskRepository.findAllTasks()
                    .map { task -> TaskWithCategory(task, findCategory(task.categoryId)) }
                    .filter { it.task.categoryId == categoryId }
                emit(tasks)
            }
        }
        return taskWithCategoryList.map { list -> list.filter { it.task.categoryId == categoryId } }
    }

    private suspend fun findCategory(categoryId: Long?): Category? {
        if (categoryId == null || categoryRepository == null) return null
        return categoryRepository.findCategoryById(categoryId)
    }
}
