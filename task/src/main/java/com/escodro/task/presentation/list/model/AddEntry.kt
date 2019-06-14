package com.escodro.task.presentation.list.model

import com.escodro.core.databinding.BindingHolder
import com.escodro.core.extension.onActionDone
import com.escodro.task.databinding.ItemAddTaskBinding

/**
 * Entry representing an item to add a new task.
 */
class AddEntry(
    private val onAddClicked: () -> Unit,
    private val onInsertTask: (String) -> Unit
) : ItemEntry(0, null) {

    override val type: Int
        get() = TYPE_ADD

    override fun bindData(holder: BindingHolder<*>) {
        val binding = holder.binding as? ItemAddTaskBinding

        binding?.imageviewItemaddCompleted?.setOnClickListener { onAddClicked() }
        binding?.edittextItemaddDescription?.onActionDone { onInsertTask(it) }
    }
}
