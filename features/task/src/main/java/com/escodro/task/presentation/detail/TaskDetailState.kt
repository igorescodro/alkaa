package com.escodro.task.presentation.detail

import com.escodro.task.model.TaskWithCategory

internal sealed class TaskDetailState {

    object Error : TaskDetailState()

    data class Loaded(val taskWithCategory: TaskWithCategory) : TaskDetailState()

}
