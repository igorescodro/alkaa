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
import timber.log.Timber

/**
 * [Fragment] responsible to show and handle all [TaskWithCategory]s.
 */
class TaskListFragment : Fragment(), TaskListAdapter.TaskItemListener {

    private val adapter = TaskListAdapter()

    private val viewModel: TaskListViewModel by viewModel()

    private val systemService: SystemService by inject()

    private var binding: FragmentTaskListBinding? = null

    private var navigator: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_task_list, container, false)
        setHasOptionsMenu(true)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        bindComponents()
        adapter.listener = this
        loadTasks()
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView()")

        adapter.listener = null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    private fun bindComponents() {
        Timber.d("bindComponents()")

        binding?.setLifecycleOwner(this)
        binding?.recyclerviewTasklistList?.adapter = adapter
        binding?.recyclerviewTasklistList?.layoutManager = getLayoutManager()
        binding?.edittextTasklistDescription?.setOnEditorActionListener(getEditorActionListener())
        binding?.viewModel = viewModel
    }

    private fun loadTasks() {
        Timber.d("loadTasks()")

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

    private fun onTaskLoaded(list: List<TaskWithCategory>) {
        Timber.d("onTaskLoaded() - Size = ${list.size}")

        adapter.updateList(list)
    }

    private fun onEmptyField() {
        Timber.d("onEmptyField()")

        binding?.edittextTasklistDescription?.error = getString(R.string.task_error_empty)
    }

    private fun onNewTaskAdded(taskWithCategory: TaskWithCategory) {
        Timber.d("onNewTaskAdded() - Task = ${taskWithCategory.task.description}")

        adapter.addItem(taskWithCategory)
    }

    private fun onTaskRemoved(taskWithCategory: TaskWithCategory) {
        Timber.d("onTaskRemoved() - Task = ${taskWithCategory.task.description}")

        adapter.removeItem(taskWithCategory)
    }

    override fun onItemClicked(taskWithCategory: TaskWithCategory) {
        Timber.d("onItemClicked() - Task = ${taskWithCategory.task.description}")

        val action = TaskListFragmentDirections.actionDetail(taskWithCategory.task)
        navigator?.navigate(action)
    }

    override fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean) {
        Timber.d("onItemCheckedChanged() - Task = ${taskWithCategory.task.description} - Value = $value")

        viewModel.updateTaskStatus(taskWithCategory.task, value)
    }

    override fun onItemLongPressed(taskWithCategory: TaskWithCategory) {
        Timber.d("onItemLongPressed() - Task = ${taskWithCategory.task.description}")

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
