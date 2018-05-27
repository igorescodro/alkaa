package com.escodro.alkaa.ui.task

import com.escodro.alkaa.data.local.model.Task

/**
 * Navigator responsible to [TaskFragment] operations.
 *
 * @author Igor Escodro on 1/4/18.
 */
interface TaskNavigator {

    /**
     * Updates the [TaskAdapter] with the new list of [Task].
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
