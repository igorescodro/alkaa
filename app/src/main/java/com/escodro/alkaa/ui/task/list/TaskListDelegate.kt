package com.escodro.alkaa.ui.task.list

import com.escodro.alkaa.data.local.model.Task

/**
 * Delegate interface responsible to [TaskListFragment] UI operations.
 *
 * @author Igor Escodro on 1/4/18.
 */
interface TaskListDelegate {

    /**
     * Updates the [TaskListAdapter] with the new list of [Task].
     *
     * @param list new list of [Task]
     */
    fun updateList(list: MutableList<Task>)

    /**
     * Called when a task without description tries to be added.
     */
    fun onEmptyField()

    /**
     * Called when a new task is added.
     *
     * @param task task added
     */
    fun onNewTaskAdded(task: Task)

    /**
     * Called when a task is removed.
     *
     * @param task task removed
     */
    fun onTaskRemoved(task: Task)
}
