package com.escodro.search.presentation

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
internal class TaskSearchItem(private val taskSearch: TaskSearch) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_search_name.text = taskSearch.title
    }

    override fun getLayout(): Int = R.layout.item_search

    override fun getId(): Long = taskSearch.id
}
