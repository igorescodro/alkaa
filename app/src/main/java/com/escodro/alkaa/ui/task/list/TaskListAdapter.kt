package com.escodro.alkaa.ui.task.list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.CheckBox
import com.escodro.alkaa.R
import com.escodro.alkaa.common.databinding.BindingRecyclerAdapter
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.ItemTaskBinding

/**
 * [RecyclerView.Adapter] to bind the [TaskWithCategory] in the [RecyclerView].
 */
class TaskListAdapter : BindingRecyclerAdapter<TaskWithCategory, ItemTaskBinding>() {

    var listener: TaskItemListener? = null

    override val layoutResource: Int
        get() = R.layout.item_task

    override fun bindData(binding: ItemTaskBinding, data: TaskWithCategory) {
        binding.task = data.task
        binding.color = data.category?.color ?: DEFAULT_LABEL_COLOR
        binding.root.setOnLongClickListener { _ -> notifyLongPressListener(data) }
        binding.root.setOnClickListener { _ -> notifyItemClickListener(data) }
        binding.checkboxItemtaskCompleted
            .setOnClickListener { view -> notifyCheckListener(view, data) }
    }

    private fun notifyItemClickListener(taskWithCategory: TaskWithCategory) {
        listener?.onItemClicked(taskWithCategory)
    }

    private fun notifyCheckListener(view: View, taskWithCategory: TaskWithCategory) {
        val checkBox: CheckBox? = view as? CheckBox
        checkBox?.isChecked?.let { listener?.onItemCheckedChanged(taskWithCategory, it) }
    }

    private fun notifyLongPressListener(taskWithCategory: TaskWithCategory): Boolean {
        listener?.onItemLongPressed(taskWithCategory)
        return true
    }

    companion object {

        private const val DEFAULT_LABEL_COLOR = "#FFFFFF"
    }

    /**
     * Listener responsible to callback interactions with [TaskWithCategory] item.
     */
    interface TaskItemListener {

        /**
         * Callback notified when the item is clicked.
         *
         * @param taskWithCategory the task that changed its status
         */
        fun onItemClicked(taskWithCategory: TaskWithCategory)

        /**
         * Callback notified when the checked status from the [CheckBox] changes.
         *
         * @param taskWithCategory the task that changed its status
         * @param value the checkbox new value
         */
        fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean)

        /**
         * Callback notified when a item is long pressed.
         *
         * @param taskWithCategory task selected
         */
        fun onItemLongPressed(taskWithCategory: TaskWithCategory)
    }
}
