package com.escodro.tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import com.escodro.tracker.model.Tracker
import com.escodro.tracker.model.mapper.TrackerMapper
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Tracker layout.
 */
class TrackerViewModel(
    private val loadTasksByPeriodUseCase: LoadCompletedTasksByPeriod,
    private val trackerMapper: TrackerMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val viewState: LiveData<TrackerUIState>
        get() = _viewState

    private val _viewState: MutableLiveData<TrackerUIState> = MutableLiveData()

    /**
     * Loads the data to be shown in the chart.
     */
    fun loadData() {
        Timber.d("loadData()")

        val disposable = loadTasksByPeriodUseCase()
            .map { trackerMapper.toTracker(it) }
            .subscribe(::onSuccess) { Timber.e(it) }

        compositeDisposable.add(disposable)
    }

    private fun onSuccess(trackerInfo: Tracker.Info) {
        Timber.d("onSuccess() = $trackerInfo")

        _viewState.value = when {
            trackerInfo.categoryList.isEmpty() -> TrackerUIState.EmptyChartState
            else -> TrackerUIState.ShowDataState(trackerInfo)
        }
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
