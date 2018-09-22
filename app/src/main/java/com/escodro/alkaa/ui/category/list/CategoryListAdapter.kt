package com.escodro.alkaa.ui.category.list

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.escodro.alkaa.R
import com.escodro.alkaa.common.databinding.BindingRecyclerAdapter
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.databinding.ItemCategoryBinding

/**
 * [RecyclerView.Adapter] to bind the [Category] in the [RecyclerView].
 */
class CategoryListAdapter : BindingRecyclerAdapter<Category, ItemCategoryBinding>() {

    var listener: CategoryListListener? = null

    override val layoutResource: Int
        get() = R.layout.item_category

    override fun bindData(binding: ItemCategoryBinding, data: Category) {
        binding.category = data
        binding.imageviewItemcategoryOptions.setOnClickListener { view ->
            notifyMenuClicked(view, data)
        }
    }

    private fun notifyMenuClicked(view: View, category: Category) {
        listener?.onOptionMenuClicked(view, category)
    }

    /**
     * Listener responsible to callback interactions with [Category] item.
     */
    interface CategoryListListener {

        /**
         * Callback notified when one item of [android.widget.PopupMenu] is clicked.
         *
         * @param view the view clicked
         * @param category the category clicked
         */
        fun onOptionMenuClicked(view: View, category: Category)
    }
}
