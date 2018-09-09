package com.escodro.alkaa.ui.category.list

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.escodro.alkaa.R
import com.escodro.alkaa.common.databinding.BindingHolder
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.databinding.ItemCategoryBinding

/**
 * [RecyclerView.Adapter] to bind the [Category] in the [RecyclerView].
 */
class CategoryListAdapter constructor(private var context: Context) :
    RecyclerView.Adapter<BindingHolder<ItemCategoryBinding>>() {

    private val categoryList: MutableList<Category> = ArrayList()

    var listener: CategoryListListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemCategoryBinding> {
        val binding = DataBindingUtil.inflate<ItemCategoryBinding>(
            LayoutInflater.from(context), R.layout.item_category, parent, false
        )

        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemCategoryBinding>, position: Int) {
        val binding = holder.binding
        val category = categoryList[position]
        binding.category = category
        binding.imageviewItemcategoryOptions.setOnClickListener { view ->
            notifyMenuClicked(view, category)
        }
    }

    override fun getItemCount(): Int = categoryList.size

    /**
     * Updates the [RecyclerView] with the given [MutableList].
     *
     * @param list list of tasks
     */
    fun updateCategoryList(list: List<Category>) {
        categoryList.clear()
        categoryList.addAll(list)
        notifyDataSetChanged()
    }

    private fun notifyMenuClicked(view: View, category: Category) {
        listener?.onOptionMenuClicked(view, category)
    }

    /**
     * Removes new [Category] from the list.
     *
     * @param category category to be removed
     */
    fun removeCategory(category: Category) {
        val position = categoryList.indexOf(category)
        categoryList.remove(category)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, categoryList.size)
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
