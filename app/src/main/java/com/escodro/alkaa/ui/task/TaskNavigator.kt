package com.escodro.alkaa.ui.task

import com.escodro.alkaa.data.local.model.Task

/**
 * Navigator responsible to [TaskActivity] operations.
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
}
