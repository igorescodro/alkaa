package com.escodro.task.presentation.detail.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.escodro.core.extension.hideKeyboard
import com.escodro.core.extension.observeOnce
import com.escodro.core.extension.setStyleDisabled
import com.escodro.core.extension.setStyleEnabled
import com.escodro.core.extension.showToast
import com.escodro.core.extension.textChangedFlow
import com.escodro.task.R
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show the Task details.
 */
internal class TaskDetailFragment : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_task_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        initComponents()
        initListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.key_update_completed_status) {
            showStateToast()
            viewModel.updateTaskStatus()
            return true
        }
        return false
    }

    private fun showStateToast() {
        val resId = if (viewModel.state.value is TaskDetailUIState.Uncompleted) {
            R.string.task_status_mark_completed
        } else {
            R.string.task_status_mark_uncompleted
        }
        context?.showToast(resId)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop()")

        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy()")

        viewModel.onDetach()
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        val taskId = arguments?.let { TaskDetailFragmentArgs.fromBundle(it).taskId }
        taskId?.let { viewModel.loadTask(it) }

        viewModel.state.observe(viewLifecycleOwner, Observer { uiState ->
            when (uiState) {
                is TaskDetailUIState.Uncompleted -> onTaskUncompleted()
                is TaskDetailUIState.Completed -> onTaskCompleted()
            }
        })

        viewModel.taskData.observeOnce(viewLifecycleOwner, Observer { task ->
            edittext_taskdetail_title.setText(task.title)
            edittext_taskdetail_description.setText(task.description)
        })
    }

    private fun initListeners() = lifecycleScope.launch {
        Timber.d("initListeners()")

        launch {
            edittext_taskdetail_title.textChangedFlow()
                .collect { text -> viewModel.updateTitle(text) }
        }

        launch {
            edittext_taskdetail_description.textChangedFlow()
                .collect { text -> viewModel.updateDescription(text) }
        }
    }

    private fun onTaskUncompleted() {
        edittext_taskdetail_title.setStyleEnabled()
    }

    private fun onTaskCompleted() {
        edittext_taskdetail_title.setStyleDisabled()
    }
}
