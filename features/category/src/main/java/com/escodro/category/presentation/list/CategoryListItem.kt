package com.escodro.category.presentation.list

import android.view.View
import com.escodro.category.R
import com.escodro.category.model.Category
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_category.view.*

/**
 * Representation of a category list item.
 *
 * @param category the category to be shown on list item
 * @param onOptionMenuClicked HFO to handle option menu click
 */
internal class CategoryListItem(
    private val category: Category,
    private val onOptionMenuClicked: (view: View, category: Category) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            category.color?.let { color -> shapeview_itemcategory_color.setShapeColor(color) }
            textview_itemcategory_description.text = category.name
            imageview_itemcategory_options.setOnClickListener { view ->
                onOptionMenuClicked(view, category)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_category

    override fun getId(): Long = category.id
}
