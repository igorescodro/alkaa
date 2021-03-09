package com.escodro.task.presentation.detail.main

import com.escodro.task.model.Task

internal sealed class TaskDetailState {

    object Loading : TaskDetailState()

    object Error : TaskDetailState()

    data class Loaded(val task: Task) : TaskDetailState()
}
