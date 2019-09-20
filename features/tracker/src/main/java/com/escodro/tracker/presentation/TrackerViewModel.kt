package com.escodro.tracker.presentation

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.tracker.LoadCompletedTracker
import com.escodro.domain.viewdata.ViewData
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to Task Tracker layout.
 */
class TrackerViewModel(private val loadTrackerUseCase: LoadCompletedTracker) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads the data to be shown in the chart.
     */
    fun loadData(onDataLoaded: (list: List<ViewData.Tracker>) -> Unit) {
        val disposable = loadTrackerUseCase().subscribe({ onDataLoaded(it) }, {})
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
