package com.escodro.alkaa.ui.task

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.ActivityTaskBinding
import com.escodro.alkaa.ui.task.TaskAdapter.TaskItemListener
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * [AppCompatActivity] responsible to show the [Task] list.
 *
 * @author Igor Escodro on 1/2/18.
 */
class TaskActivity : AppCompatActivity(), TaskNavigator, TaskItemListener {

    private val adapter: TaskAdapter by inject()

    private val viewModel: TaskViewModel by viewModel()

    private lateinit var binding: ActivityTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task)
        binding.setLifecycleOwner(this)

        bindComponents(binding)
        adapter.listener = this
        viewModel.loadTasks()
    }

    private fun bindComponents(binding: ActivityTaskBinding) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = getLayoutManager()
        binding.editText.setOnEditorActionListener(getEditorActionListener())
        binding.viewModel = viewModel
    }

    private fun getLayoutManager() =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private fun getEditorActionListener(): TextView.OnEditorActionListener {
        return TextView.OnEditorActionListener { _, action, _ ->
            var result = false
            if (action == EditorInfo.IME_ACTION_DONE) {
                viewModel.addTask()
                result = true
            }
            return@OnEditorActionListener result
        }
    }

    override fun updateList(list: MutableList<Task>) =
            adapter.updateTaskList(list)

    override fun onEmptyField() {
        binding.editText.error = "Empty field"
    }

    override fun onNewTaskAdded(task: Task) {
        adapter.addTask(task)
    }

    override fun onTaskRemoved(task: Task) {
        adapter.removeTask(task)
    }

    override fun onItemCheckedChanged(task: Task, value: Boolean) =
            viewModel.updateTaskStatus(task, value)

    override fun onLongPressItem(task: Task) {
        val options = arrayOf("Delete")

        val builder = AlertDialog.Builder(this)
        builder.setTitle(task.description)
        builder.setItems(options, { _, i ->
            when (i) {
                0 -> viewModel.deleteTask(task)
            }
        })
        builder.show()
    }
}
