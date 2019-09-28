package com.escodro.tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    val viewState: LiveData<TrackerUIState>
        get() = _viewState

    private val _viewState: MutableLiveData<TrackerUIState> = MutableLiveData()

    /**
     * Loads the data to be shown in the chart.
     */
    fun loadData() {
        Timber.d("loadData()")

        val disposable = loadTrackerUseCase().subscribe(::onSuccess) { Timber.e(it) }
        compositeDisposable.add(disposable)
    }

    private fun onSuccess(list: List<ViewData.Tracker>) {
        Timber.d("onSuccess() - Size = ${list.size}")

        _viewState.value = when {
            list.isEmpty() -> TrackerUIState.EmptyChartState
            else -> TrackerUIState.ShowDataState(list, getTaskCount(list))
        }
    }

    private fun getTaskCount(list: List<ViewData.Tracker>) =
        list.sumBy { tracker -> tracker.taskCount ?: 0 }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
