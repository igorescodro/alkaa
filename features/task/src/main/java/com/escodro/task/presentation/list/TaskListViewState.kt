package com.escodro.task.presentation.list

import com.escodro.task.model.TaskWithCategory
import kotlinx.collections.immutable.ImmutableList

/**
 * Presentation entity to represent the view states of Task Section.
 */
internal sealed class TaskListViewState {

    object Loading : TaskListViewState()

    data class Error(val cause: Throwable) : TaskListViewState()

    data class Loaded(val items: ImmutableList<TaskWithCategory>) : TaskListViewState()

    object Empty : TaskListViewState()
}
