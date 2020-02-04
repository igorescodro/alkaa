package com.escodro.task.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.escodro.domain.usecase.task.GetTask
import com.escodro.task.mapper.TaskMapper
import com.escodro.task.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Provides the [MutableLiveData] of [Task] to be used in all screens that needs to share the same
 * information reactivity.
 */
internal class TaskDetailProvider(
    private val getTaskUseCase: GetTask,
    private val taskMapper: TaskMapper
) {

    val taskData: LiveData<Task>
        get() = mutableTaskData

    private var mutableTaskData = MutableLiveData<Task>()

    /**
     * Loads the task based on the given id.
     *
     * @param taskId task id
     */
    fun loadTask(taskId: Long?, viewModelScope: CoroutineScope) {
        if (taskId == null) {
            Timber.e("loadTask - Task id is null")
            return
        }

        viewModelScope.launch {
            getTaskUseCase(taskId).collect {
                mutableTaskData.value = taskMapper.toView(it)
            }
        }
    }

    /**
     * Clears the provider.
     */
    fun clear() {
        Timber.d("clear()")
        mutableTaskData = MutableLiveData()
    }
}
