package com.escodro.alkaa.ui.task

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.ActivityTaskBinding
import com.escodro.alkaa.di.Injector
import com.escodro.alkaa.ui.task.TaskAdapter.OnItemCheckedChangeListener
import javax.inject.Inject

/**
 * [AppCompatActivity] responsible to show the [Task] list.
 *
 * @author Igor Escodro on 1/2/18.
 */
class TaskActivity : AppCompatActivity(), TaskNavigator, OnItemCheckedChangeListener {

    @Inject lateinit var adapter: TaskAdapter

    private lateinit var viewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Injector.applicationComponent.inject(this)

        val binding: ActivityTaskBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_task)

        initViewModel()
        bindComponents(binding)
        adapter.listener = this
        viewModel.loadTasks()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    private fun initViewModel() {
        val factory = TaskViewModel.Factory(this)
        viewModel = ViewModelProviders.of(this, factory).get(TaskViewModel::class.java)
    }

    private fun bindComponents(binding: ActivityTaskBinding) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = getLayoutManager()
        binding.viewModel = viewModel
    }

    private fun getLayoutManager() =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    fun onClick(view: View) =
            viewModel.addTask()

    override fun updateList(list: MutableList<Task>) =
            adapter.updateTaskList(list)

    override fun onItemCheckedChanged(task: Task, value: Boolean) =
            viewModel.updateTaskStatus(task, value)
}
