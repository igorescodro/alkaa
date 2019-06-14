package com.escodro.task.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.escodro.core.databinding.BindingHolder
import com.escodro.domain.viewdata.ViewData
import com.escodro.task.R
import com.escodro.task.presentation.list.model.AddEntry
import com.escodro.task.presentation.list.model.ItemEntry
import com.escodro.task.presentation.list.model.TaskEntry

/**
 * [RecyclerView.Adapter] to bind the [com.escodro.domain.viewdata.ViewData.TaskWithCategory] in the [RecyclerView].
 */
internal class TaskListAdapter(
    private val onItemClicked: (ViewData.TaskWithCategory) -> Unit,
    private val onItemLongPressed: (ViewData.TaskWithCategory) -> Boolean,
    private val onItemCheckedChanged: (ViewData.TaskWithCategory, Boolean) -> Unit,
    private val onInsertTask: (String) -> Unit,
    private val onAddClicked: () -> Unit
) : ListAdapter<ItemEntry, BindingHolder<*>>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<*> {
        val inflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, getLayout(viewType), parent, false)
        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder<*>, position: Int) {
        val item = getItem(position)
        item.bindData(holder)
    }

    override fun getItemViewType(position: Int) =
        getItem(position).type

    /**
     * Submits a new list to be diffed, and displayed.
     *
     * If a list is already being displayed, a diff will be computed on a background thread, which
     * will dispatch Adapter.notifyItem events on the main thread.
     *
     * @param list The new list to be displayed.
     */
    fun updateList(list: List<ViewData.TaskWithCategory>, showAddButton: Boolean) {
        val itemList: MutableList<ItemEntry> = list
            .map { toItemEntry(it) }
            .toMutableList()

        if (showAddButton) {
            itemList.add(getAddEntry())
        }

        submitList(itemList)
    }

    private fun getLayout(type: Int) =
        if (type == ItemEntry.TYPE_ADD) R.layout.item_add_task else R.layout.item_task

    private fun toItemEntry(task: ViewData.TaskWithCategory) =
        TaskEntry(
            task,
            onItemClicked = onItemClicked,
            onItemLongPressed = onItemLongPressed,
            onItemCheckedChanged = onItemCheckedChanged
        )

    private fun getAddEntry() =
        AddEntry(onInsertTask = onInsertTask, onAddClicked = onAddClicked)
}
