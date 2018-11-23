package com.escodro.alkaa.ui.task.list

import androidx.recyclerview.widget.DiffUtil
import com.escodro.alkaa.ui.task.list.model.ItemEntry

/**
 * Comparator used to check if the item loaded is already in the list.
 */
class TaskDiffCallback : DiffUtil.ItemCallback<ItemEntry>() {

    override fun areItemsTheSame(oldItem: ItemEntry, newItem: ItemEntry) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ItemEntry, newItem: ItemEntry) =
        oldItem.task == newItem.task
}
