package com.escodro.alkaa.ui.task.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.escodro.alkaa.R
import com.escodro.alkaa.common.databinding.BindingHolder
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.ItemTaskBinding

/**
 * [RecyclerView.Adapter] to bind the [TaskWithCategory] in the [RecyclerView].
 */
class TaskListAdapter(
    private val onItemClicked: (TaskWithCategory) -> Unit,
    private val onItemLongPressed: (TaskWithCategory) -> Boolean,
    private val onItemCheckedChanged: (TaskWithCategory, Boolean) -> Unit
) : ListAdapter<TaskWithCategory, BindingHolder<ItemTaskBinding>>(TaskDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemTaskBinding> {

        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ItemTaskBinding>(inflater, R.layout.item_task, parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemTaskBinding>, position: Int) {
        val binding = holder.binding
        val data = getItem(position)
        binding.task = data.task
        binding.color = data.category?.color ?: DEFAULT_LABEL_COLOR
        binding.isAlarmVisible = data.task.dueDate != null
        binding.cardviewItemtaskBackground.setOnClickListener { onItemClicked(data) }
        binding.cardviewItemtaskBackground.setOnLongClickListener { onItemLongPressed(data) }
        binding.checkboxItemtaskCompleted.setOnClickListener { view ->
            val isChecked = (view as? CheckBox)?.isChecked ?: false
            onItemCheckedChanged(data, isChecked)
        }
    }

    companion object {

        private const val DEFAULT_LABEL_COLOR = "#FFFFFF"
    }
}
