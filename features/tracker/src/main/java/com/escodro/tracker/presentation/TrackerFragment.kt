package com.escodro.tracker.presentation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escodro.domain.viewdata.ViewData
import com.escodro.tracker.R
import com.escodro.tracker.di.injectDynamicFeature
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.google.android.play.core.splitcompat.SplitCompat
import kotlinx.android.synthetic.main.fragment_tracker.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Temporary fragment for the new dynamic module.
 */
class TrackerFragment : Fragment() {

    private val trackerViewModel: TrackerViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        injectDynamicFeature()
        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        SplitCompat.install(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        trackerViewModel.loadData(updateChart = ::updateChart, updateCount = ::updateCount)
    }

    private fun updateChart(list: List<ViewData.Tracker>) {
        Timber.d("updateChart() - Size = ${list.size}")

        val dataSet = PieDataSet(convertToEntry(list), "")
        dataSet.colors = getDataSetColors(list)

        piechart_tracker.apply {
            data = createPieData(dataSet)
            description.isEnabled = false
            invalidate()
        }
    }

    private fun updateCount(taskCount: Int) {
        Timber.d("updateCount() - Total = $taskCount")

        textview_tracker_tasks.text =
            resources.getQuantityString(R.plurals.tracker_message_title, taskCount, taskCount)
    }

    private fun getDataSetColors(trackerList: List<ViewData.Tracker>) =
        trackerList.map {
            it.categoryColor?.let { color -> Color.parseColor(color) } ?: Color.GRAY
        }

    private fun convertToEntry(trackerList: List<ViewData.Tracker>) = trackerList.map {
        val count = it.taskCount?.toFloat() ?: 0F
        val name = it.categoryName ?: "No category"
        PieEntry(count, name)
    }

    private fun createPieData(dataSet: PieDataSet) = PieData(dataSet).apply {
        setValueFormatter(DefaultValueFormatter(0))
        setValueTextSize(CHART_VALUE_TEXT_SIZE)
        setValueTextColor(Color.WHITE)
    }

    companion object {

        private const val CHART_VALUE_TEXT_SIZE = 16F
    }
}
