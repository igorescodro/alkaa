package com.escodro.search.presentation

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import com.escodro.core.extension.setStyleDisable
import com.escodro.search.R
import com.escodro.search.model.TaskSearch
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_search.view.*

/**
 * Represents the item in the Task Search list.
 *
 * @param taskSearch the item to be bind
 */
internal class TaskSearchItem(
    private val taskSearch: TaskSearch,
    private val onItemClicked: (Long) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            textview_search_name.text = taskSearch.title
            setOnClickListener { onItemClicked(taskSearch.id) }

            var circleColor = taskSearch.categoryColor
            if (taskSearch.completed) {
                viewHolder.itemView.textview_search_name.setStyleDisable()
                circleColor = Color.GRAY
            }

            circleColor?.let {
                view_search_circle.colorFilter =
                    PorterDuffColorFilter(it, PorterDuff.Mode.MULTIPLY)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_search

    override fun getId(): Long = taskSearch.id
}
