package com.escodro.category.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.escodro.domain.viewdata.ViewData

/**
 * Comparator used to check if the item loaded is already in the list.
 */
class CategoryDiffCallback : DiffUtil.ItemCallback<ViewData.Category>() {

    override fun areItemsTheSame(oldItem: ViewData.Category, newItem: ViewData.Category) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ViewData.Category, newItem: ViewData.Category) =
        oldItem == newItem
}
