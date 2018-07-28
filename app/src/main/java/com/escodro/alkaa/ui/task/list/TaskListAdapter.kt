package com.escodro.alkaa.ui.task.list

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.escodro.alkaa.R
import com.escodro.alkaa.common.viewholder.BindingHolder
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.ItemTaskBinding

/**
 * [RecyclerView.Adapter] to bind the [TaskWithCategory] in the [RecyclerView].
 */
class TaskListAdapter constructor(private var context: Context) :
    RecyclerView.Adapter<BindingHolder<ItemTaskBinding>>() {

    private val taskList: MutableList<TaskWithCategory> = ArrayList()

    var listener: TaskItemListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BindingHolder<ItemTaskBinding> {
        val binding = DataBindingUtil.inflate<ItemTaskBinding>(
            LayoutInflater.from(context),
            R.layout.item_task,
            parent,
            false
        )

        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<ItemTaskBinding>, position: Int) {
        val binding = holder.binding
        val taskWithCategory = taskList[position]
        binding.task = taskWithCategory.task
        binding.color = taskWithCategory.category?.color ?: DEFAULT_LABEL_COLOR
        binding.root.setOnLongClickListener { _ -> notifyLongPressListener(taskWithCategory) }
        binding.root.setOnClickListener { _ -> notifyItemClickListener(taskWithCategory) }
        binding.checkboxItemtaskCompleted
            .setOnClickListener { view -> notifyCheckListener(view, taskWithCategory) }
    }

    override fun getItemCount(): Int = taskList.size

    /**
     * Updates the [RecyclerView] with the given [MutableList].
     *
     * @param list list of tasks
     */
    fun updateTaskList(list: MutableList<TaskWithCategory>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Adds a new [TaskWithCategory] in the list.
     *
     * @param taskWithCategory task to be added
     */
    fun addTask(taskWithCategory: TaskWithCategory) {
        taskList.add(taskWithCategory)
        notifyItemChanged(itemCount)
    }

    /**
     * Removes new [TaskWithCategory] from the list.
     *
     * @param taskWithCategory task to be removed
     */
    fun removeTask(taskWithCategory: TaskWithCategory) {
        val position = taskList.indexOf(taskWithCategory)
        taskList.remove(taskWithCategory)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, taskList.size)
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
