package com.escodro.category.presentation.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.escodro.category.R
import com.escodro.category.common.BindingHolder
import com.escodro.category.databinding.ItemCategoryTmpBinding
import com.escodro.domain.viewdata.ViewData

/**
 * [RecyclerView.Adapter] to bind the [Category] in the [RecyclerView].
 */
class CategoryListAdapter(val onOptionMenuClicked: (view: View, category: ViewData.Category) -> Unit) :
    ListAdapter<ViewData.Category, BindingHolder<ItemCategoryTmpBinding>>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemCategoryTmpBinding> {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryTmpBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_category_tmp, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemCategoryTmpBinding>, position: Int) {
        val item = getItem(position)
        holder.binding.category = item
        holder.binding.imageviewItemcategoryOptions.setOnClickListener { view ->
            onOptionMenuClicked(view, item)
        }
    }
}
