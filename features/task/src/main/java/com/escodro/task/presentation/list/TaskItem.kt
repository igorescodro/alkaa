package com.escodro.task.presentation.list

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.escodro.core.extension.formatRelativeDate
import com.escodro.core.extension.setStyleDisabled
import com.escodro.task.R
import com.escodro.task.model.TaskWithCategory
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.Calendar
import java.util.Objects
import kotlinx.android.synthetic.main.item_task.view.*

/**
 * Entry representing an item with task information.
 */
internal class TaskItem(
    private val data: TaskWithCategory,
    private val onItemClicked: (TaskWithCategory) -> Unit,
    private val onItemLongPressed: (TaskWithCategory) -> Boolean,
    private val onItemCheckedChanged: (TaskWithCategory, Boolean) -> Unit
) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val task = data.task
            val category = data.category

            textview_itemtask_description.text = task.title
            category?.color?.let { view_itemtask_color.setBackgroundColor(category.color) }
            textview_itemtask_alarm.visibility = getVisibilityFromBoolean(task.dueDate != null)
            imageview_itemtask_repeating.visibility = getVisibilityFromBoolean(task.isRepeating)
            textview_itemtask_alarm.text = getRelativeDate(context, task.dueDate)
            checkbox_itemtask_completed.isChecked = task.completed

            initListeners(this)
            updateCompletedStyle(this)
        }
    }

    override fun getLayout(): Int = R.layout.item_task

    private fun initListeners(view: View) {
        view.cardview_itemtask_background.setOnClickListener { onItemClicked(data) }
        view.cardview_itemtask_background.setOnLongClickListener { onItemLongPressed(data) }
        view.checkbox_itemtask_completed.setOnClickListener { checkbox ->
            val isChecked = (checkbox as? CheckBox)?.isChecked ?: false
            onItemCheckedChanged(data, isChecked)
        }
    }

    private fun updateCompletedStyle(view: View) {
        if (data.task.completed) {
            view.textview_itemtask_description.setStyleDisabled()
            view.textview_itemtask_alarm.setStyleDisabled()
        }
    }

    private fun getVisibilityFromBoolean(isVisible: Boolean): Int =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }

    override fun getId(): Long = data.task.id

    override fun equals(other: Any?): Boolean {
        val otherItem = other as? TaskItem ?: return super.equals(other)
        return otherItem.data == data
    }

    override fun hashCode(): Int = Objects.hashCode(data)

    private fun getRelativeDate(context: Context, calendar: Calendar?): String =
        context.formatRelativeDate(calendar?.timeInMillis)
}
