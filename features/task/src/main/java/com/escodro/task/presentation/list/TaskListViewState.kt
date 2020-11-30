package com.escodro.task.presentation.list

import com.escodro.task.model.TaskWithCategory

/**
 * Presentation entity to represent the view states of Task Section.
 *
 * @param items the list of tasks
 */
data class TaskListViewState(val items: List<TaskWithCategory>)
