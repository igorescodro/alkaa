package com.escodro.alkaa.ui.task.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.FragmentTaskListBinding
import com.escodro.alkaa.di.SystemService
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * [Fragment] responsible to show and handle all [TaskWithCategory]s.
 */
class TaskListFragment : Fragment(), TaskListAdapter.TaskItemListener {

    private val adapter: TaskListAdapter by inject()

    private val viewModel: TaskListViewModel by viewModel()

    private val systemService: SystemService by inject()

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
        loadTasks()
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        adapter.listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    private fun bindComponents() {
        binding?.setLifecycleOwner(this)
        binding?.recyclerviewTasklistList?.adapter = adapter
        binding?.recyclerviewTasklistList?.layoutManager = getLayoutManager()
        binding?.edittextTasklistDescription?.setOnEditorActionListener(getEditorActionListener())
        binding?.viewModel = viewModel
    }

    private fun loadTasks() {
        val itemId = arguments?.getLong(TaskListFragment.EXTRA_CATEGORY_ID) ?: 0
        val taskName = arguments?.getString(
            TaskListFragment.EXTRA_CATEGORY_NAME, getString(R.string.drawer_menu_all_tasks)
        )
        viewModel.loadTasks(itemId, onTasksLoaded = { onTaskLoaded(it) })
        binding?.textviewTasklistCategory?.text = taskName
    }

    private fun getLayoutManager() =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

    private fun getEditorActionListener(): TextView.OnEditorActionListener =
        TextView.OnEditorActionListener { _, action, _ ->
            var result = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                viewModel.addTask(
                    onEmptyField = { onEmptyField() },
                    onNewTaskAdded = { onNewTaskAdded(it) }
                )
                systemService.getInputMethodManager()?.hideSoftInputFromWindow(view?.windowToken, 0)
                result = true
            }
            result
        }

    private fun onTaskLoaded(list: List<TaskWithCategory>) =
        adapter.updateTaskList(list)

    private fun onEmptyField() {
        binding?.edittextTasklistDescription?.error = getString(R.string.task_error_empty)
    }

    private fun onNewTaskAdded(taskWithCategory: TaskWithCategory) {
        adapter.addTask(taskWithCategory)
    }

    private fun onTaskRemoved(taskWithCategory: TaskWithCategory) {
        adapter.removeTask(taskWithCategory)
    }

    override fun onItemClicked(taskWithCategory: TaskWithCategory) {
        val action = TaskListFragmentDirections.actionDetail(taskWithCategory.task)
        navigator?.navigate(action)
    }

    override fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean) {
        viewModel.updateTaskStatus(taskWithCategory.task, value)
    }

    override fun onItemLongPressed(taskWithCategory: TaskWithCategory) {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(taskWithCategory.task.description)
        builder?.setItems(R.array.task_dialog_options) { _,
            item ->
            when (item) {
                0 -> viewModel.deleteTask(taskWithCategory, onTaskRemoved = { onTaskRemoved(it) })
            }
        }
        builder?.show()
    }

    companion object {

        const val EXTRA_CATEGORY_ID = "category_id"

        const val EXTRA_CATEGORY_NAME = "category_name"
    }
}
