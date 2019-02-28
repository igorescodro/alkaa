package com.escodro.alkaa.ui.task.detail.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.hideKeyboard
import com.escodro.alkaa.common.extension.textChangedObservable
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskDetailBinding
import com.escodro.alkaa.ui.task.detail.TaskDetailProvider
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_task_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show the [Task] details.
 */
class TaskDetailFragment : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModel()

    private val taskProvider: TaskDetailProvider by inject()

    private val compositeDisposable = CompositeDisposable()

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

        taskProvider.clear()
        compositeDisposable.clear()
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        binding?.setLifecycleOwner(this)
        binding?.viewModel = viewModel

        val taskId = arguments?.let { TaskDetailFragmentArgs.fromBundle(it).taskId }
        taskProvider.loadTask(taskId)
    }

    private fun initListeners() {
        Timber.d("initListeners()")

        val titleDisposable = edittext_taskdetail_title.textChangedObservable()
            .subscribe { text -> viewModel.updateTitle(text) }

        val descDisposable = edittext_taskdetail_description.textChangedObservable()
            .subscribe { text -> viewModel.updateDescription(text) }

        compositeDisposable.addAll(titleDisposable, descDisposable)
    }
}
