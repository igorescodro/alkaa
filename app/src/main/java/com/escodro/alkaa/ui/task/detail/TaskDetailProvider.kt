package com.escodro.alkaa.ui.task.detail

import androidx.lifecycle.MutableLiveData
import com.escodro.alkaa.data.local.model.Task

/**
 * Provides the [MutableLiveData] of [Task] to be used in [TaskDetailFragment] layout.
 */
class TaskDetailProvider {

    val taskData = MutableLiveData<Task>()
}
