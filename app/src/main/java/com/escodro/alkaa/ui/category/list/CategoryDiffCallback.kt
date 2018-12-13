package com.escodro.alkaa.ui.category.list

import androidx.recyclerview.widget.DiffUtil
import com.escodro.alkaa.data.local.model.Category

/**
 * Comparator used to check if the item loaded is already in the list.
 */
class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) =
        oldItem == newItem
}
