package com.escodro.alkaa.ui.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.hideKeyboard
import com.escodro.alkaa.common.extension.itemDialog
import com.escodro.alkaa.common.extension.items
import com.escodro.alkaa.common.extension.withDelay
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.databinding.FragmentTaskListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show and handle all [TaskWithCategory]s.
 */
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModel()

    private var binding: FragmentTaskListBinding? = null

    private var navigator: NavController? = null

    private val adapter = TaskListAdapter(
        onItemClicked = ::onItemClicked,
        onItemLongPressed = ::onItemLongPressed,
        onItemCheckedChanged = ::onItemCheckedChanged,
        onInsertTask = ::onInsertTask
    )

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
        loadTasks()
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    override fun onStop() {
        super.onStop()

        viewModel.onDetach()
    }

    private fun bindComponents() {
        Timber.d("bindComponents()")

        binding?.setLifecycleOwner(this)
        binding?.recyclerviewTasklistList?.adapter = adapter
        binding?.recyclerviewTasklistList?.layoutManager = getLayoutManager()
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
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private fun onInsertTask(description: String) {
        hideKeyboard()
        withDelay(INSERT_DELAY) { viewModel.addTask(description) }
    }

    private fun onTaskLoaded(list: List<TaskWithCategory>) {
        Timber.d("onTaskLoaded() - Size = ${list.size}")

        adapter.updateList(list)
    }

    private fun onItemClicked(taskWithCategory: TaskWithCategory) {
        Timber.d("onItemClicked() - Task = ${taskWithCategory.task.description}")

        val action = TaskListFragmentDirections.actionDetail(taskWithCategory.task)
        navigator?.navigate(action)
    }

    private fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean) {
        Timber.d("onItemCheckedChanged() - Task = ${taskWithCategory.task.description} - Value = $value")

        viewModel.updateTaskStatus(taskWithCategory.task, value)
    }

    private fun onItemLongPressed(taskWithCategory: TaskWithCategory): Boolean {
        Timber.d("onItemLongPressed() - Task = ${taskWithCategory.task.description}")

        itemDialog(taskWithCategory.task.description) {
            items(R.array.task_dialog_options) { item ->
                when (item) {
                    0 -> viewModel.deleteTask(taskWithCategory)
                }
            }
        }.show()

        return true
    }

    companion object {

        private const val INSERT_DELAY = 200L

        const val EXTRA_CATEGORY_ID = "category_id"

        const val EXTRA_CATEGORY_NAME = "category_name"
    }
}
