package com.escodro.alkaa.ui.task

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import com.escodro.alkaa.R
import com.escodro.alkaa.common.viewholder.BindingHolder
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.ItemTaskBinding

/**
 * [RecyclerView.Adapter] to bind the [Task] in the [RecyclerView].
 *
 * @author Igor Escodro on 1/3/18.
 */
class TaskAdapter constructor(private var context: Context) :
    RecyclerView.Adapter<BindingHolder<ItemTaskBinding>>() {

    private val taskList: MutableList<Task> = ArrayList()

    lateinit var listener: TaskItemListener

    override fun onBindViewHolder(holder: BindingHolder<ItemTaskBinding>, position: Int) {
        val binding = holder.binding
        val task = taskList[position]
        binding.task = task
        binding.root.setOnLongClickListener { _ -> notifyLongPressListener(task) }
        binding.checkbox.setOnClickListener({ view -> notifyCheckListener(view, task) })
    }

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

    override fun getItemCount(): Int = taskList.size

    /**
     * Updates the [RecyclerView] with the given [MutableList].
     *
     * @param list list of tasks
     *
     */
    fun updateTaskList(list: MutableList<Task>) {
        taskList.clear()
        taskList.addAll(list)
        notifyDataSetChanged()
    }

    /**
     * Adds a new [Task] in the list.
     *
     * @param task task to be added
     */
    fun addTask(task: Task) {
        taskList.add(task)
        notifyItemChanged(itemCount)
    }

    /**
     * Removes new [Task] from the list.
     *
     * @param task task to be removed
     */
    fun removeTask(task: Task) {
        val position = taskList.indexOf(task)
        taskList.remove(task)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, taskList.size)
    }

    private fun notifyCheckListener(view: View, task: Task) {
        if ((view as CheckBox).isChecked) {
            listener.onItemCheckedChanged(task, true)
        } else {
            listener.onItemCheckedChanged(task, false)
        }
    }

    private fun notifyLongPressListener(task: Task): Boolean {
        listener.onLongPressItem(task)
        return true
    }

    /**
     * Listener responsible to callback interactions with [Task] item.
     */
    interface TaskItemListener {

        /**
         * Callback notified when the checked status from the [CheckBox] changes.
         *
         * @param task the task that changed its status
         * @param value the checkbox new value
         */
        fun onItemCheckedChanged(task: Task, value: Boolean)

        /**
         * Callback notified when a item is long pressed.
         *
         * @param task task selected
         */
        fun onLongPressItem(task: Task)
    }
}
