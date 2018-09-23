package com.escodro.alkaa.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.escodro.alkaa.R
import com.escodro.alkaa.common.view.LabelRadioButton
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

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
        Timber.d("onCreateView()")

        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_detail, container, false
            )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        initComponents()
        initListeners()
        viewModel.loadCategories(onCategoryListLoaded = ::updateCategoryList)
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        binding?.setLifecycleOwner(this)
        binding?.viewModel = viewModel

        task = TaskDetailFragmentArgs.fromBundle(arguments).task
        viewModel.task.value = task
        (activity as? AppCompatActivity)?.supportActionBar?.title = task?.description
    }

    private fun initListeners() {
        Timber.d("initListeners()")

        binding?.srgTaskdetailList?.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { radioGroup, position ->
                updateTaskWithCategory(radioGroup, position)
            })
    }

    private fun updateCategoryList(list: List<Category>) {
        Timber.d("updateCategoryList() - Size = ${list.size}")

        binding?.srgTaskdetailList?.addAll(list)

        val checked = list.withIndex().firstOrNull {
            it.value.id == task?.categoryId
        }
        checked?.let { binding?.srgTaskdetailList?.setChecked(it.index) }
    }

    private fun updateTaskWithCategory(radioGroup: RadioGroup?, position: Int) {
        Timber.d("updateTaskWithCategory() - Position = $position")

        val checked = radioGroup?.findViewById<LabelRadioButton>(position)
        val categoryId = checked?.tag as? Long ?: 0

        if (task?.categoryId != categoryId) {
            task?.categoryId = categoryId
            task?.let { viewModel.updateTask(it) }
        }
    }
}
