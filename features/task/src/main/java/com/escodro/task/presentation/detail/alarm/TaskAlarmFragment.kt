package com.escodro.task.presentation.detail.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.escodro.core.extension.showDateTimePicker
import com.escodro.core.extension.showToast
import com.escodro.task.R
import com.escodro.task.databinding.FragmentTaskDetailAlarmBinding
import java.util.Calendar
import kotlinx.android.synthetic.main.fragment_task_detail_alarm.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show the [com.escodro.domain.viewdata.ViewData.Task] alarm.
 */
internal class TaskAlarmFragment : Fragment() {

    private val viewModel: TaskAlarmViewModel by viewModel()

    private var binding: FragmentTaskDetailAlarmBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_task_detail_alarm, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        binding?.lifecycleOwner = this
        binding?.viewModel = viewModel
        initListeners()
    }

    private fun initListeners() {
        btn_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }
        textview_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }
        chip_taskdetail_date.setOnCloseIconClickListener { removeAlarm() }
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
