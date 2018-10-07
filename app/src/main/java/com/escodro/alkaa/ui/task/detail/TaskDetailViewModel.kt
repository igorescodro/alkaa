package com.escodro.alkaa.ui.task.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.task.alarm.TaskAlarmManager
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 *
 * Created by Igor Escodro on 31/5/18.
 */
class TaskDetailViewModel(
    private val contract: TaskDetailContract,
    private val alarmManager: TaskAlarmManager
) : ViewModel() {

    val taskData = MutableLiveData<Task>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Load all categories.
     */
    fun loadCategories(onCategoryListLoaded: (list: List<Category>) -> Unit) {
        val disposable = contract.loadAllCategories().subscribe { onCategoryListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Updates the given task.
     *
     * @param task the task to be updated
     */
    fun updateTask(task: Task) {
        taskData.value = task
        val disposable = contract.updateTask(task).subscribe()
        compositeDisposable.add(disposable)

        taskData.value?.dueDate?.let {
            alarmManager.scheduleTask(task)
        }
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
