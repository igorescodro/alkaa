package com.escodro.task.presentation.list

import com.escodro.task.model.TaskWithCategory

internal data class TaskStateHandler(
    val state: TaskListViewState = TaskListViewState.Empty,
    val onCheckedChanged: (TaskWithCategory) -> Unit = {},
    val onItemClicked: (Long) -> Unit = {},
)
