package com.escodro.task.presentation.list.model

import com.escodro.core.databinding.BindingHolder
import com.escodro.task.model.TaskWithCategory

/**
 * Abstract reference of an item in [com.escodro.task.presentation.list.TaskListFragment].
 */
internal abstract class ItemEntry(val id: Long, val task: TaskWithCategory?) {

    /**
     * Represent the view type.
     */
    abstract val type: Int

    /**
     * Binds the data based on the data binding layout.
     */
    abstract fun bindData(holder: BindingHolder<*>)

    companion object {

        /**
         * Task type.
         */
        const val TYPE_TASK = 1

        /**
         * Add a new Task type.
         */
        const val TYPE_ADD = 2
    }
}
