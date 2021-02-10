package com.escodro.task.presentation.detail

import com.escodro.task.model.Category
import com.escodro.task.model.Task

internal sealed class TaskDetailState {

    object Error : TaskDetailState()

    data class Loaded(val task: Task, val categoryList: List<Category>) : TaskDetailState()
}
