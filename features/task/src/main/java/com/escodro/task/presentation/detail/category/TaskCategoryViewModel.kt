package com.escodro.task.presentation.detail.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.usecase.task.UpdateTask
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.model.Category
import com.escodro.task.presentation.detail.TaskDetailProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Category layout.
 */
internal class TaskCategoryViewModel(
    taskProvider: TaskDetailProvider,
    private val loadAllCategoriesUseCase: LoadAllCategories,
    private val categoryMapper: CategoryMapper,
    private val updateTaskUseCase: UpdateTask,
    private val taskMapper: TaskMapper
) : ViewModel() {

    val taskData = taskProvider.taskData

    /**
     * Load all categories.
     */
    fun loadCategories(onCategoryListLoaded: (list: List<Category>) -> Unit) =
        viewModelScope.launch {
            loadAllCategoriesUseCase()
                .map { categoryMapper.toView(it) }
                .collect {
                    onCategoryListLoaded(it)
                }
        }

    /**
     * Updates the task category.
     *
     * @param categoryId the task category id
     */
    fun updateCategory(categoryId: Long?) {
        Timber.d("updateCategory() - $categoryId")

        taskData.value?.let {
            if (it.categoryId == categoryId) {
                return
            }

            viewModelScope.launch {
                taskData.value?.copy(categoryId = categoryId)?.let { task ->
                    updateTaskUseCase(taskMapper.toDomain(task))
                }
            }
        }
    }
}
