package com.escodro.alkaa.ui.task.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.hideKeyboard
import com.escodro.alkaa.common.extension.showDateTimePicker
import com.escodro.alkaa.common.extension.showToast
import com.escodro.alkaa.common.extension.textChangedObservable
import com.escodro.alkaa.common.view.LabelRadioButton
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.data.local.model.Task
import com.escodro.alkaa.databinding.FragmentTaskDetailBinding
import com.escodro.alkaa.ui.main.MainTaskViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_task_detail.*
import kotlinx.android.synthetic.main.view_scrollable_radio_group.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.Calendar

/**
 * [Fragment] responsible to show the [Task] details.
 */
class TaskDetailFragment : Fragment() {

    private val viewModel: TaskDetailViewModel by viewModel()

    private val sharedViewModel: MainTaskViewModel by sharedViewModel()

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
        viewModel.loadCategories(onCategoryListLoaded = ::updateCategoryList)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop()")

        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy()")

        compositeDisposable.clear()
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        binding?.setLifecycleOwner(this)
        binding?.viewModel = viewModel

        viewModel.taskData.value = arguments?.let { TaskDetailFragmentArgs.fromBundle(it).task }
        sharedViewModel.updateTitle(null)
    }

    private fun initListeners() {
        Timber.d("initListeners()")

        srg_radiogroup_list.setOnCheckedChangeListener { radioGroup, position ->
            updateTaskWithCategory(radioGroup, position)
        }

        btn_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }
        textview_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }

        val titleDisposable = edittext_taskdetail_title.textChangedObservable()
            .subscribe { text -> viewModel.updateTitle(text) }

        val descDisposable = edittext_taskdetail_description.textChangedObservable()
            .subscribe { text -> viewModel.updateDescription(text) }

        chip_taskdetail_date.setOnCloseIconClickListener { removeAlarm() }

        compositeDisposable.addAll(titleDisposable, descDisposable)
    }

    private fun updateCategoryList(list: List<Category>) {
        Timber.d("updateCategoryList() - Size = ${list.size}")

        binding?.srgTaskdetailList?.updateList(list)

        val checked = list.asSequence().withIndex().firstOrNull {
            it.value.id == viewModel.taskData.value?.categoryId
        }
        checked?.let { binding?.srgTaskdetailList?.setChecked(it.index) }
    }

    private fun updateTaskWithCategory(radioGroup: RadioGroup?, position: Int) {
        Timber.d("updateTaskWithCategory() - Position = $position")

        val checked = radioGroup?.findViewById<LabelRadioButton>(position)
        val categoryId = checked?.tag as? Long ?: 0
        viewModel.updateCategory(categoryId)
    }

    private fun updateTaskWithDueDate(calendar: Calendar) {
        Timber.d("updateTaskWithDueDate() - Calendar = ${calendar.time}")

        viewModel.setAlarm(calendar)
    }

    private fun removeAlarm() {
        Timber.d("removeAlarm()")

        context?.showToast(R.string.task_details_alarm_removed)
        viewModel.removeAlarm()
    }
}
