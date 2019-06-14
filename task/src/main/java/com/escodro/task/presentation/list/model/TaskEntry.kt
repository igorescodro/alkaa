package com.escodro.task.presentation.list.model

import android.widget.CheckBox
import com.escodro.core.databinding.BindingHolder
import com.escodro.core.extension.setStyleDisable
import com.escodro.domain.viewdata.ViewData
import com.escodro.task.databinding.ItemTaskBinding

/**
 * Entry representing an item with task information.
 */
class TaskEntry(
    private val data: ViewData.TaskWithCategory,
    private val onItemClicked: (ViewData.TaskWithCategory) -> Unit,
    private val onItemLongPressed: (ViewData.TaskWithCategory) -> Boolean,
    private val onItemCheckedChanged: (ViewData.TaskWithCategory, Boolean) -> Unit
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
        get() = TYPE_TASK

    companion object {

        private const val DEFAULT_LABEL_COLOR = "#FFFFFF"
    }
}
