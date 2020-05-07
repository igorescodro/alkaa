package com.escodro.task.presentation.detail.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.escodro.core.extension.format
import com.escodro.core.extension.itemDialog
import com.escodro.core.extension.items
import com.escodro.core.extension.showDateTimePicker
import com.escodro.core.extension.showToast
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import java.util.Calendar
import kotlinx.android.synthetic.main.fragment_task_detail_alarm.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show the [com.escodro.domain.viewdata.ViewData.Task] alarm.
 */
internal class TaskAlarmFragment : Fragment() {

    private val viewModel: TaskAlarmViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        return inflater.inflate(R.layout.fragment_task_detail_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        initListeners()
    }

    private fun initListeners() {
        btn_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }
        textview_taskdetail_date.setOnClickListener { showDateTimePicker(::updateTaskWithDueDate) }
        chip_taskdetail_date.setOnCloseIconClickListener { removeAlarm() }
        btn_taskdetail_repeating.setOnClickListener { showIntervalDialog() }
        textview_taskdetail_repeating.setOnClickListener { showIntervalDialog() }

        viewModel.taskData.observe(viewLifecycleOwner, Observer { task ->
            chip_taskdetail_date.text = task.dueDate?.format()
            val alarmIndex = task.alarmInterval?.index ?: 0
            val intervalString = resources.getStringArray(R.array.task_alarm_repeating)[alarmIndex]
            Timber.d("Current interval = $intervalString")
            textview_taskdetail_repeating.text = intervalString
        })

        viewModel.chipVisibility.observe(viewLifecycleOwner, Observer { isVisible ->
            chip_taskdetail_date.visibility = getVisibilityFromBoolean(isVisible)
            textview_taskdetail_date.visibility = getVisibilityFromBoolean(isVisible.not())
            btn_taskdetail_repeating.visibility = getVisibilityFromBoolean(isVisible)
            textview_taskdetail_repeating.visibility = getVisibilityFromBoolean(isVisible)
        })
    }

    private fun showIntervalDialog() {
        itemDialog(R.string.task_alarm_dialog_title) {
            items(R.array.task_alarm_repeating) { item ->
                val interval = AlarmInterval.values().find { it.index == item }
                interval?.let { viewModel.setRepeating(it) }
            }
        }?.show()
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

    private fun getVisibilityFromBoolean(isVisible: Boolean): Int =
        if (isVisible) {
            View.VISIBLE
        } else {
            View.GONE
        }
}
