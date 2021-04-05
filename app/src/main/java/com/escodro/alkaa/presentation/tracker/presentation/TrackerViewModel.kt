package com.escodro.alkaa.presentation.tracker.presentation

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.presentation.tracker.mapper.TrackerMapper
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

internal class TrackerViewModel(
    private val loadCompletedTasksByPeriod: LoadCompletedTasksByPeriod,
    private val trackerMapper: TrackerMapper
) : ViewModel() {

    fun loadTracker(): Flow<TrackerViewState> = flow {
        loadCompletedTasksByPeriod()
            .map { task -> trackerMapper.toTracker(task) }
            .catch { error -> emit(TrackerViewState.Error(error)) }
            .collect { trackerInfo ->
                val state = if (trackerInfo.categoryList.isNotEmpty()) {
                    TrackerViewState.Loaded(trackerInfo)
                } else {
                    TrackerViewState.Empty
                }
                emit(state)
            }
    }
}
