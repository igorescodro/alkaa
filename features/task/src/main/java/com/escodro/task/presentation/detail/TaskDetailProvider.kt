package com.escodro.task.presentation.detail

import androidx.lifecycle.LiveData
import com.escodro.task.model.Task
import kotlinx.coroutines.CoroutineScope

/**
 * Contract to provide the data to be used in all screens that needs to share the same information
 * reactivity.
 */
internal interface TaskDetailProvider {

    val taskData: LiveData<Task>

    /**
     * Loads the task based on the given id.
     *
     * @param taskId task id
     */
    fun loadTask(taskId: Long?, viewModelScope: CoroutineScope)

    /**
     * Clears the provider.
     */
    fun clear()
}
