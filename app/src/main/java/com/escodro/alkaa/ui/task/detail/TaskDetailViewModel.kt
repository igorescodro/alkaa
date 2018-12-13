package com.escodro.alkaa.ui.task.detail

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.common.extension.notify
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.ui.task.alarm.TaskAlarmManager
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.util.Calendar

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 */
class TaskDetailViewModel(
    private val contract: TaskDetailContract,
    private val alarmManager: TaskAlarmManager
) : ViewModel() {

    val taskData = MutableLiveData<Task>()

    val chipVisibility = MediatorLiveData<Boolean>()

    private val compositeDisposable = CompositeDisposable()

    init {
        chipVisibility.addSource(taskData) { chipVisibility.value = it.dueDate != null }
    }

    /**
     * Load all categories.
     */
    fun loadCategories(onCategoryListLoaded: (list: List<Category>) -> Unit) {
        val disposable = contract.loadAllCategories().subscribe { onCategoryListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Updates the task title.
     *
     * @param title the task title
     */
    fun updateTitle(title: String) {
        Timber.d("updateTitle() - $title")

        if (title.isEmpty()) {
            return
        }

        taskData.value?.let {
            taskData.value?.title = title
            val disposable = contract.updateTask(it).subscribe()
            compositeDisposable.add(disposable)
        }
    }

    /**
     * Updates the task description.
     *
     * @param description the task description
     */
    fun updateDescription(description: String) {
        Timber.d("updateDescription() - $description")

        taskData.value?.let {
            taskData.value?.description = description
            val disposable = contract.updateTask(it).subscribe()
            compositeDisposable.add(disposable)
        }
    }

    /**
     * Updates the task category.
     *
     * @param categoryId the task category id
     */
    fun updateCategory(categoryId: Long) {
        Timber.d("updateCategory() - $categoryId")

        taskData.value?.let {
            if (it.categoryId == categoryId) {
                return
            }

            taskData.value?.categoryId = categoryId
            val disposable = contract.updateTask(it).subscribe()
            compositeDisposable.add(disposable)
        }
    }

    /**
     * Sets an alarm to the task.
     *
     * @param alarm the date and time to ring the calendar
     */
    fun setAlarm(alarm: Calendar) {
        Timber.d("setAlarm()")

        taskData.value?.let {
            it.dueDate = alarm
            alarmManager.scheduleTaskAlarm(it)
            updateTask(it)
        }
        taskData.notify()
    }

    /**
     * Removes the task alarm.
     */
    fun removeAlarm() {
        Timber.d("removeAlarm()")

        taskData.value?.let {
            it.dueDate = null
            alarmManager.cancelTaskAlarm(it.id)
            updateTask(it)
        }
        taskData.notify()
    }

    private fun updateTask(task: Task) {
        Timber.d("updateTask() - $task")

        val disposable = contract.updateTask(task).subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
