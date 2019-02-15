package com.escodro.alkaa.ui.task.detail.main

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentDetailBinding].
 */
class TaskDetailViewModel(private val taskProvider: TaskDetailProvider) : ViewModel() {

    val taskData = taskProvider.taskData

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
}
