package com.escodro.alkaa.ui.task.list

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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.FragmentTaskListBinding
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * [Fragment] responsible to show and handle all [TaskWithCategory]s.
 */
class TaskListFragment : Fragment(), TaskListDelegate, TaskListAdapter.TaskItemListener {

    private val adapter: TaskListAdapter by inject()

    private val viewModel: TaskViewModel by viewModel()

    private var binding: FragmentTaskListBinding? = null

    private var navigator: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_task_list, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindComponents()
        adapter.listener = this
        viewModel.delegate = this
        viewModel.loadTasks()
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_preference -> navigator?.navigate(R.id.action_preference)
            R.id.action_category -> navigator?.navigate(R.id.action_category)
        }
        return true
    }

    private fun bindComponents() {
        binding?.setLifecycleOwner(this)
        binding?.recyclerviewTasklistList?.adapter = adapter
        binding?.recyclerviewTasklistList?.layoutManager = getLayoutManager()
        binding?.edittextTasklistDescription?.setOnEditorActionListener(getEditorActionListener())
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

    override fun updateList(list: MutableList<TaskWithCategory>) =
        adapter.updateTaskList(list)

    override fun onEmptyField() {
        binding?.edittextTasklistDescription?.error = getString(R.string.task_error_empty)
    }

    override fun onNewTaskAdded(taskWithCategory: TaskWithCategory) {
        adapter.addTask(taskWithCategory)
    }

    override fun onTaskRemoved(taskWithCategory: TaskWithCategory) {
        adapter.removeTask(taskWithCategory)
    }

    override fun onItemClicked(taskWithCategory: TaskWithCategory) {
        val bundle = bundleOf(EXTRA_TASK to taskWithCategory.task)
        navigator?.navigate(R.id.action_detail, bundle)
    }

    override fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean) {
        viewModel.updateTaskStatus(taskWithCategory.task, value)
    }

    override fun onItemLongPressed(taskWithCategory: TaskWithCategory) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(taskWithCategory.task.description)
        builder?.setItems(R.array.task_dialog_options, { _,
            item ->
            when (item) {
                0 -> viewModel.deleteTask(taskWithCategory)
            }
        })
        builder?.show()
    }

    companion object {

        const val EXTRA_TASK = "task"
    }
}
