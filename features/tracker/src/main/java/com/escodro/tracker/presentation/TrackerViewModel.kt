package com.escodro.tracker.presentation

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.tracker.LoadCompletedTracker
import com.escodro.domain.viewdata.ViewData
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Tracker layout.
 */
class TrackerViewModel(private val loadTrackerUseCase: LoadCompletedTracker) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads the data to be shown in the chart.
     */
    fun loadData(
        updateChart: (list: List<ViewData.Tracker>) -> Unit,
        updateCount: (count: Int) -> Unit
    ) {
        val disposable = loadTrackerUseCase()
            .subscribe(
                {
                    updateChart(it)
                    updateCount(getTaskCount(it))
                },
                { Timber.e(it) })

        compositeDisposable.add(disposable)
    }

    private fun getTaskCount(list: List<ViewData.Tracker>) =
        list.sumBy { tracker -> tracker.taskCount ?: 0 }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
