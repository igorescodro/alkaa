package com.escodro.task.presentation.detail.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.escodro.task.model.Task
import com.escodro.task.presentation.detail.TaskDetailProvider
import kotlinx.coroutines.CoroutineScope

internal class FakeTaskDetailProvider : TaskDetailProvider {

    private val _taskData = MutableLiveData<Task>()

    override val taskData: LiveData<Task>
        get() = _taskData

    var isTaskCompleted: Boolean = false

    override fun loadTask(taskId: Long?, viewModelScope: CoroutineScope) {
        val task = Task(id = taskId!!, title = "test", completed = isTaskCompleted)
        _taskData.value = task
    }

    override fun clear() {
        _taskData.value = null
    }
}
