package com.escodro.alkaa.ui.task.detail

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import com.escodro.alkaa.R
import com.escodro.alkaa.common.view.LabelRadioButton
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskDetailBinding
import org.koin.android.architecture.ext.viewModel

/**
 * [Fragment] responsible to show the [Task] details.
 *
 * Created by Igor Escodro on 31/5/18.
 */
class TaskDetailFragment : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModel()

    private var binding: FragmentTaskDetailBinding? = null

    private var task: Task? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_detail, container, false
            )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents()
        initListeners()
        viewModel.loadCategories(onCategoryListLoaded = { updateCategoryList(it) })
    }

    private fun initComponents() {
        binding?.setLifecycleOwner(this)
        binding?.viewModel = viewModel

        task = TaskDetailFragmentArgs.fromBundle(arguments).task
        viewModel.task.value = task
        (activity as? AppCompatActivity)?.supportActionBar?.title = task?.description
    }

    private fun initListeners() {
        binding?.srgTaskdetailList?.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { radioGroup, position ->
                updateTaskWithCategory(radioGroup, position)
            })
    }

    private fun updateCategoryList(list: List<Category>) {
        binding?.srgTaskdetailList?.addAll(list)

        val checked = list.withIndex().firstOrNull {
            it.value.id == task?.categoryId
        }
        checked?.let { binding?.srgTaskdetailList?.setChecked(it.index) }
    }

    private fun updateTaskWithCategory(radioGroup: RadioGroup?, position: Int) {
        val checked = radioGroup?.findViewById<LabelRadioButton>(position)
        val categoryId = checked?.tag as? Long ?: 0

        if (task?.categoryId != categoryId) {
            task?.categoryId = categoryId
            task?.let { viewModel.updateTask(it) }
        }
    }
}
