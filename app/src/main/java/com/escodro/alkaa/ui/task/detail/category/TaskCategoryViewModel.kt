package com.escodro.alkaa.ui.task.detail.category

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Category layout.
 */
class TaskCategoryViewModel(
    private val contract: TaskCategoryContract,
    private val taskProvider: TaskDetailProvider
) : ViewModel() {

    val taskData = taskProvider.taskData

    private val compositeDisposable = CompositeDisposable()

    /**
     * Load all categories.
     */
    fun loadCategories(onCategoryListLoaded: (list: List<Category>) -> Unit) {
        val disposable = contract.loadAllCategories().subscribe { onCategoryListLoaded(it) }
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
