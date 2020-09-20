package com.escodro.tracker.presentation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.escodro.tracker.R
import com.escodro.tracker.di.injectDynamicFeature
import com.escodro.tracker.model.Tracker
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        SplitCompat.install(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        trackerViewModel.loadData()
        trackerViewModel.viewState.observe(viewLifecycleOwner, Observer(::renderViewState))
    }

    private fun renderViewState(state: TrackerUIState) {
        Timber.d("renderViewState() - State = $state")

        when (state) {
            is TrackerUIState.ShowDataState -> showData(state.trackerInfo)
            is TrackerUIState.EmptyChartState -> showEmptyView()
        }
    }

    private fun showEmptyView() {
        Timber.d("showEmptyView()")

        image_tracker_empty.visibility = View.VISIBLE
        piechart_tracker.visibility = View.INVISIBLE
        updateCount()
    }

    private fun showData(trackerInfo: Tracker.Info) {
        Timber.d("showData()")

        image_tracker_empty.visibility = View.INVISIBLE
        piechart_tracker.visibility = View.VISIBLE
        updateChart(trackerInfo.categoryList)
        updateCount(trackerInfo.totalCount)
    }

    private fun updateChart(list: List<Tracker.Category>) {
        Timber.d("updateChart() - Size = ${list.size}")

        val dataSet = PieDataSet(convertToEntry(list), "")
        dataSet.colors = getDataSetColors(list)

        piechart_tracker.apply {
            data = createPieData(dataSet)
            description.isEnabled = false
            invalidate()
        }
    }

    private fun updateCount(taskCount: Int = 0) {
        Timber.d("updateCount() - Total = $taskCount")

        textview_tracker_tasks.text =
            resources.getQuantityString(R.plurals.tracker_message_title, taskCount, taskCount)
    }

    private fun getDataSetColors(list: List<Tracker.Category>) =
        list.map { it.categoryColor?.let { color -> Color.parseColor(color) } ?: Color.GRAY }

    private fun convertToEntry(list: List<Tracker.Category>) = list.map {
        val count = it.taskCount.toFloat()
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
