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
import com.escodro.alkaa.common.extension.showDateTimePicker
import com.escodro.alkaa.common.extension.showToast
import com.escodro.alkaa.common.view.LabelRadioButton
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskDetailBinding
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.android.synthetic.main.view_scrollable_radio_group.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.Calendar

/**
 * [Fragment] responsible to show the [Task] details.
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
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_detail, container, false)
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
        viewModel.taskData.value = task
        (activity as? AppCompatActivity)?.supportActionBar?.title = task?.description
    }

    private fun initListeners() {
        Timber.d("initListeners()")

        srg_radiogroup_list.setOnCheckedChangeListener { radioGroup, position ->
            updateTaskWithCategory(radioGroup, position)
        }

        btn_taskdetail_date.setOnClickListener { _ -> showDateTimePicker(::updateTaskWithDueDate) }

        btn_taskdetail_remove_alarm.setOnClickListener { _ -> removeAlarm() }
    }

    private fun updateCategoryList(list: List<Category>) {
        Timber.d("updateCategoryList() - Size = ${list.size}")

        binding?.srgTaskdetailList?.addAll(list)

        val checked = list.asSequence().withIndex().firstOrNull {
            it.value.id == task?.categoryId
        }
        checked?.let { binding?.srgTaskdetailList?.setChecked(it.index) }
    }

    private fun updateTaskWithCategory(radioGroup: RadioGroup?, position: Int) {
        Timber.d("updateTaskWithCategory() - Position = $position")

        val checked = radioGroup?.findViewById<LabelRadioButton>(position)
        val categoryId = checked?.tag as? Long ?: 0

        task?.let { task ->
            if (task.categoryId != categoryId) {
                task.categoryId = categoryId
                viewModel.updateTask(task)
            }
        }
    }

    private fun updateTaskWithDueDate(calendar: Calendar) {
        Timber.d("updateTaskWithDueDate() - Calendar = ${calendar.time}")

        task?.let {
            it.dueDate = calendar
            viewModel.updateTask(it)
        }
    }

    private fun removeAlarm() {
        Timber.d("removeAlarm()")
        context?.showToast(R.string.task_details_alarm_removed)

        task?.let {
            it.dueDate = null
            viewModel.updateTask(it)
        }
    }
}
