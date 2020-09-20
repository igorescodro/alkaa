package com.escodro.tracker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import com.escodro.tracker.mapper.TrackerMapper
import com.escodro.tracker.model.Tracker
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Tracker layout.
 */
internal class TrackerViewModel(
    private val loadTasksByPeriodUseCase: LoadCompletedTasksByPeriod,
    private val trackerMapper: TrackerMapper
) : ViewModel() {

    internal val viewState: LiveData<TrackerUIState>
        get() = _viewState

    private val _viewState: MutableLiveData<TrackerUIState> = MutableLiveData()

    /**
     * Loads the data to be shown in the chart.
     */
    fun loadData() = viewModelScope.launch {
        Timber.d("loadData()")

        loadTasksByPeriodUseCase()
            .map { trackerMapper.toTracker(it) }
            .collect { onSuccess(it) }
    }

    private fun onSuccess(trackerInfo: Tracker.Info) {
        Timber.d("onSuccess() = $trackerInfo")

        _viewState.value = if (trackerInfo.categoryList.isEmpty()) {
            TrackerUIState.EmptyChartState
        } else {
            TrackerUIState.ShowDataState(trackerInfo)
        }
    }
}
