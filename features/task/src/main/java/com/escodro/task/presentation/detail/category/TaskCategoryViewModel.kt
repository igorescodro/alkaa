package com.escodro.task.presentation.detail.category

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.task.mapper.CategoryMapper
import com.escodro.task.model.Category
import com.escodro.task.presentation.detail.TaskDetailProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Category layout.
 */
internal class TaskCategoryViewModel(
    private val taskProvider: TaskDetailProvider,
    private val loadAllCategoriesUseCase: LoadAllCategories,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    val taskData = taskProvider.taskData

    private val compositeDisposable = CompositeDisposable()

    /**
     * Load all categories.
     */
    fun loadCategories(onCategoryListLoaded: (list: List<Category>) -> Unit) {
        val disposable = loadAllCategoriesUseCase.test()
            .map { categoryMapper.toView(it) }
            .subscribe { onCategoryListLoaded(it) }

        compositeDisposable.add(disposable)
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

            taskData.value?.categoryId = categoryId
            taskProvider.updateTask(it)
        }
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
