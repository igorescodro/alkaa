package com.escodro.alkaa.ui.task.list.model

import android.widget.CheckBox
import com.escodro.alkaa.common.databinding.BindingHolder
import com.escodro.alkaa.common.extension.setStyleDisable
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.ItemTaskBinding

/**
 * Entry representing an item with task information.
 */
class TaskEntry(
    private val data: TaskWithCategory,
    private val onItemClicked: (TaskWithCategory) -> Unit,
    private val onItemLongPressed: (TaskWithCategory) -> Boolean,
    private val onItemCheckedChanged: (TaskWithCategory, Boolean) -> Unit
) : ItemEntry(data.task.id, data) {

    override fun bindData(holder: BindingHolder<*>) {
        val binding = holder.binding as? ItemTaskBinding

        binding?.apply {
            task = data.task
            color = data.category?.color ?: DEFAULT_LABEL_COLOR
            isAlarmVisible = data.task.dueDate != null
            cardviewItemtaskBackground.setOnClickListener { onItemClicked(data) }
            cardviewItemtaskBackground.setOnLongClickListener { onItemLongPressed(data) }
            checkboxItemtaskCompleted.setOnClickListener { view ->
                val isChecked = (view as? CheckBox)?.isChecked ?: false
                onItemCheckedChanged(data, isChecked)
            }

            if (data.task.completed) {
                textviewItemtaskDescription.setStyleDisable()
                textviewItemtaskAlarm.setStyleDisable()
            }
        }
    }

    override val type: Int
        get() = ItemEntry.TYPE_TASK

    companion object {

        private const val DEFAULT_LABEL_COLOR = "#FFFFFF"
    }
}
