package com.escodro.task.presentation.list

import com.escodro.core.extension.onActionDone
import com.escodro.task.R
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.Objects
import kotlinx.android.synthetic.main.item_add_task.view.*

/**
 * Entry representing an item to add a new task.
 */
internal class TaskAddItem(
    private val onAddClicked: () -> Unit,
    private val onInsertTask: (String) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            imageview_itemadd_completed.setOnClickListener { onAddClicked() }
            edittext_itemadd_description.onActionDone { text -> onInsertTask(text) }
        }
    }

    override fun getLayout(): Int = R.layout.item_add_task

    override fun getId(): Long = 0L

    override fun equals(other: Any?): Boolean {
        val otherItem = other as? TaskAddItem ?: return super.equals(other)
        return otherItem.id == id
    }

    override fun hashCode(): Int = Objects.hashCode(id)
}
