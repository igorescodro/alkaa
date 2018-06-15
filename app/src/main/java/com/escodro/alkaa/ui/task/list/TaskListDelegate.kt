package com.escodro.alkaa.ui.task.list

import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.data.local.model.TaskWithCategory

/**
 * Delegate interface responsible to [TaskListFragment] UI operations.
 */
interface TaskListDelegate {

    /**
     * Updates the [TaskListAdapter] with the new list of [Task].
     *
     * @param list new list of [Task]
     */
    fun updateList(list: MutableList<TaskWithCategory>)

    /**
     * Called when a task without description tries to be added.
     */
    fun onEmptyField()

    /**
     * Called when a new task is added.
     *
     * @param taskWithCategory task added
     */
    fun onNewTaskAdded(taskWithCategory: TaskWithCategory)

    /**
     * Called when a task is removed.
     *
     * @param taskWithCategory task removed
     */
    fun onTaskRemoved(taskWithCategory: TaskWithCategory)
}
