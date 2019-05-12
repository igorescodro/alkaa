package com.escodro.alkaa.ui.task.detail.main

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import com.escodro.alkaa.ui.task.list.TaskListFragment
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 */
class TaskDetailViewModel(private val taskProvider: TaskDetailProvider) : ViewModel() {

    val taskData = taskProvider.taskData

    /**
     * Loads the Task to be handled by the [ViewModel]s.
     *
     * @param id the task id
     */
    fun loadTask(id: Long) {
        Timber.d("loadTask() - $id")
        taskProvider.loadTask(id)
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
            taskProvider.updateTask(it)
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
            taskProvider.updateTask(it)
        }
    }

    /**
     * Clears the [ViewModel] when the [TaskListFragment] is not visible to user.
     */
    fun onDetach() {
        taskProvider.clear()
    }
}
