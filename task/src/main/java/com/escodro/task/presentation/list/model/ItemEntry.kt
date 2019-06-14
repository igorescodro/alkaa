package com.escodro.task.presentation.list.model

import com.escodro.core.databinding.BindingHolder
import com.escodro.domain.viewdata.ViewData

/**
 * Abstract reference of an item in [com.escodro.alkaa.ui.task.list.TaskListFragment].
 */
abstract class ItemEntry(val id: Long, val task: ViewData.TaskWithCategory?) {

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
