package com.escodro.task.presentation.detail.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.escodro.core.extension.hideKeyboard
import com.escodro.core.extension.textChangedFlow
import com.escodro.task.R
import com.escodro.task.databinding.FragmentTaskDetailBinding
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

    private var binding: FragmentTaskDetailBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        initComponents()
        initListeners()
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

        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel

        val taskId = arguments?.let { TaskDetailFragmentArgs.fromBundle(it).taskId }
        taskId?.let { viewModel.loadTask(it) }
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
}
