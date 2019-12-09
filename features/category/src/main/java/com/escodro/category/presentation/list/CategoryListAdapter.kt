package com.escodro.category.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.escodro.category.R
import com.escodro.category.databinding.ItemCategoryBinding
import com.escodro.category.model.Category
import com.escodro.core.databinding.BindingHolder

/**
 * [RecyclerView.Adapter] to bind the [com.escodro.domain.viewdata.ViewData.Category] in the [RecyclerView].
 */
internal class CategoryListAdapter(val onOptionMenuClicked: (view: View, category: Category) -> Unit) :
    ListAdapter<Category, BindingHolder<ItemCategoryBinding>>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemCategoryBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_category, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemCategoryBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.category = item
        holder.binding.imageviewItemcategoryOptions.setOnClickListener { view ->
            onOptionMenuClicked(view, item)
        }
    }
}
