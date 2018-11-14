package com.escodro.alkaa.ui.task.list

import androidx.recyclerview.widget.DiffUtil
import com.escodro.alkaa.data.local.model.TaskWithCategory

/**
 * Comparator used to check if the item loaded is already in the list.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<TaskWithCategory>() {

    override fun areItemsTheSame(oldItem: TaskWithCategory, newItem: TaskWithCategory) =
        oldItem.task.id == newItem.task.id

    override fun areContentsTheSame(oldItem: TaskWithCategory, newItem: TaskWithCategory) =
        oldItem == newItem
}
