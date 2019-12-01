package com.escodro.category.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.escodro.category.model.Category

/**
 * Comparator used to check if the item loaded is already in the list.
 */
internal class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Category, newItem: Category) =
        oldItem == newItem
}
