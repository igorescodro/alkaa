package com.escodro.alkaa.ui.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.hideKeyboard
import com.escodro.alkaa.common.extension.itemDialog
import com.escodro.alkaa.common.extension.items
import com.escodro.alkaa.common.extension.showKeyboard
import com.escodro.alkaa.common.extension.showSnackbar
import com.escodro.alkaa.common.extension.withDelay
import com.escodro.alkaa.data.local.model.TaskWithCategory
import com.escodro.alkaa.ui.main.MainTaskViewModel
import kotlinx.android.synthetic.main.fragment_task_list.*
import kotlinx.android.synthetic.main.item_add_task.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show and handle all [TaskWithCategory]s.
 */
class TaskListFragment : Fragment() {

    private val viewModel: TaskListViewModel by viewModel()

    private val sharedViewModel: MainTaskViewModel by sharedViewModel()

    private var navigator: NavController? = null

    private var itemId: Long = 0

    private val adapter = TaskListAdapter(
        onItemClicked = ::onItemClicked,
        onItemLongPressed = ::onItemLongPressed,
        onItemCheckedChanged = ::onItemCheckedChanged,
        onInsertTask = ::onInsertTask,
        onAddClicked = ::onAddClicked
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        bindComponents()
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_menu, menu)
    }

    override fun onStart() {
        super.onStart()

        loadTasks()
    }

    override fun onStop() {
        super.onStop()

        viewModel.onDetach()
    }

    private fun bindComponents() {
        Timber.d("bindComponents()")

        recyclerview_tasklist_list?.adapter = adapter
        recyclerview_tasklist_list?.layoutManager = getLayoutManager()
    }

    private fun loadTasks() {
        Timber.d("loadTasks()")

        val defaultTitle = getString(R.string.drawer_menu_all_tasks)
        itemId = arguments?.getLong(TaskListFragment.EXTRA_CATEGORY_ID) ?: 0
        val taskName = arguments?.getString(TaskListFragment.EXTRA_CATEGORY_NAME) ?: defaultTitle

        viewModel.loadTasks(itemId, onTasksLoaded = { onTaskLoaded(it) })
        sharedViewModel.updateTitle(taskName)
    }

    private fun getLayoutManager() =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private fun onInsertTask(description: String) {
        hideKeyboard()
        withDelay(INSERT_DELAY) { viewModel.addTask(description) }
    }

    private fun onTaskLoaded(list: List<TaskWithCategory>) {
        Timber.d("onTaskLoaded() - Size = ${list.size}")

        val showAddButton = itemId != TaskListContract.COMPLETED_TASKS
        adapter.updateList(list, showAddButton)
    }

    private fun onItemClicked(taskWithCategory: TaskWithCategory) {
        Timber.d("onItemClicked() - Task = ${taskWithCategory.task.title}")

        val action = TaskListFragmentDirections.actionDetail(taskWithCategory.task)
        navigator?.navigate(action)
    }

    private fun onItemCheckedChanged(taskWithCategory: TaskWithCategory, value: Boolean) {
        Timber.d("onItemCheckedChanged() - Task = ${taskWithCategory.task.title} - Value = $value")

        viewModel.updateTaskStatus(taskWithCategory.task, value)

        if (value) {
            constraint_tasklist_root.showSnackbar(R.string.task_snackbar_completed)
        }
    }

    private fun onItemLongPressed(taskWithCategory: TaskWithCategory): Boolean {
        Timber.d("onItemLongPressed() - Task = ${taskWithCategory.task.title}")

        itemDialog(taskWithCategory.task.title) {
            items(R.array.task_dialog_options) { item ->
                when (item) {
                    0 -> viewModel.deleteTask(taskWithCategory)
                }
            }
        }.show()

        return true
    }

    private fun onAddClicked() {
        Timber.d("onAddClicked")

        edittext_itemadd_description.requestFocus()
        showKeyboard()
    }

    companion object {

        private const val INSERT_DELAY = 200L

        const val EXTRA_CATEGORY_ID = "category_id"

        const val EXTRA_CATEGORY_NAME = "category_name"
    }
}
