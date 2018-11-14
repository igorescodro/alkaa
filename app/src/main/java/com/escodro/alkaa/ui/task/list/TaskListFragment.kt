package com.escodro.alkaa.ui.task.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.hideKeyboard
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
        onItemCheckedChanged = ::onItemCheckedChanged
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
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)

    private fun getEditorActionListener(): TextView.OnEditorActionListener =
        TextView.OnEditorActionListener { _, action, _ ->
            var result = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                viewModel.addTask(onEmptyField = ::onEmptyField)
                hideKeyboard()
                result = true
            }
            result
        }

    private fun onTaskLoaded(list: List<TaskWithCategory>) {
        Timber.d("onTaskLoaded() - Size = ${list.size}")

        adapter.submitList(list)
    }

    private fun onEmptyField() {
        Timber.d("onEmptyField()")

        binding?.edittextTasklistDescription?.error = getString(R.string.task_error_empty)
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

        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.setTitle(taskWithCategory.task.description)
        builder?.setItems(R.array.task_dialog_options) { _,
            item ->
            when (item) {
                0 -> viewModel.deleteTask(taskWithCategory)
            }
        }
        builder?.show()

        return true
    }

    companion object {

        const val EXTRA_CATEGORY_ID = "category_id"

        const val EXTRA_CATEGORY_NAME = "category_name"
    }
}
