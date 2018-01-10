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
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [RecyclerView.Adapter] to bind the [Task] in the [RecyclerView].
 *
 * @author Igor Escodro on 1/3/18.
 */

/*
IMPORTANT: Using the AAC, the Adapter MUST be @Singleton, otherwise it will lose its reference on
screen rotation.
*/
@Singleton
class TaskAdapter @Inject constructor(var context: Context) :
        RecyclerView.Adapter<BindingHolder<ItemTaskBinding>>() {

    private val taskList: MutableList<Task> = ArrayList()

    lateinit var listener: OnItemCheckedChangeListener

    override fun onBindViewHolder(holder: BindingHolder<ItemTaskBinding>?, position: Int) {
        val binding = holder?.binding
        val task = taskList[position]
        binding?.task = task
        binding?.checkbox?.setOnClickListener({ view -> notifyListener(view, task) })
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingHolder<ItemTaskBinding> {
        val binding = DataBindingUtil.inflate<ItemTaskBinding>(
                LayoutInflater.from(context),
                R.layout.item_task,
                parent,
                false)

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

    private fun notifyListener(view: View, task: Task) {
        if ((view as CheckBox).isChecked) {
            listener.onItemCheckedChanged(task, true)
        } else {
            listener.onItemCheckedChanged(task, false)
        }
    }

    /**
     * Callback to notify when the checked status from the [CheckBox] changes.
     */
    interface OnItemCheckedChangeListener {

        /**
         * Callback to notify when the checked status from the [CheckBox] changes.
         *
         * @param task the task that changed its status
         * @param value the checkbox new value
         */
        fun onItemCheckedChanged(task: Task, value: Boolean)
    }
}
