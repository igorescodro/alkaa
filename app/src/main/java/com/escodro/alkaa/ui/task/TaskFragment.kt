package com.escodro.alkaa.ui.task

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskBinding
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * [Fragment] responsible to show and handle all [Task]s.
 */
class TaskFragment : Fragment(), TaskNavigator, TaskAdapter.TaskItemListener {

    private val adapter: TaskAdapter by inject()

    private val viewModel: TaskViewModel by viewModel()

    private var binding: FragmentTaskBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_task, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindComponents()

        adapter.listener = this
        viewModel.loadTasks()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_preference ->
                NavHostFragment.findNavController(this).navigate(R.id.action_preference)
        }
        return true
    }

    private fun bindComponents() {
        binding?.setLifecycleOwner(this)
        binding?.recyclerView?.adapter = adapter
        binding?.recyclerView?.layoutManager = getLayoutManager()
        binding?.editText?.setOnEditorActionListener(getEditorActionListener())
        binding?.viewModel = viewModel
    }

    private fun getLayoutManager() =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    private fun getEditorActionListener(): TextView.OnEditorActionListener =
        TextView.OnEditorActionListener { _, action, _ ->
            var result = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                viewModel.addTask()
                result = true
            }
            result
        }

    override fun updateList(list: MutableList<Task>) =
        adapter.updateTaskList(list)

    override fun onEmptyField() {
        binding?.editText?.error = "Empty field"
    }

    override fun onNewTaskAdded(task: Task) {
        adapter.addTask(task)
    }

    override fun onTaskRemoved(task: Task) {
        adapter.removeTask(task)
    }

    override fun onItemClicked(task: Task) {
        val bundle = bundleOf(EXTRA_TASK to task)
        NavHostFragment.findNavController(this).navigate(R.id.action_detail, bundle)
    }

    override fun onItemCheckedChanged(task: Task, value: Boolean) {
        viewModel.updateTaskStatus(task, value)
    }

    override fun onItemLongPressed(task: Task) {
        val options = arrayOf("Delete")

        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(task.description)
        builder?.setItems(options, { _, i ->
            when (i) {
                0 -> viewModel.deleteTask(task)
            }
        })
        builder?.show()
    }

    companion object {

        const val EXTRA_TASK = "task"
    }
}
