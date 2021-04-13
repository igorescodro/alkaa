package com.escodro.tracker.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.tracker.model.Tracker
import com.escodro.tracker.presentation.components.TaskGraph
import com.escodro.tracker.presentation.components.TaskTrackerList
import org.koin.androidx.compose.getViewModel

@Composable
internal fun TrackerContent(viewModel: TrackerViewModel = getViewModel()) {
    val data by remember { viewModel.loadTracker() }
        .collectAsState(initial = TrackerViewState.Loading)

    when (data) {
        TrackerViewState.Empty -> {
            // TODO
        }
        is TrackerViewState.Error -> {
            // TODO
        }
        is TrackerViewState.Loaded ->
            TrackerLoadedContent((data as TrackerViewState.Loaded).trackerInfo)
        TrackerViewState.Loading -> {
            // TODO
        }
    }
}

@Composable
private fun TrackerLoadedContent(trackerInfo: Tracker.Info) {
    Column {
        TaskGraph(
            list = trackerInfo.categoryInfoList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(2F)
                .padding(24.dp)
        )
        TaskTrackerList(
            list = trackerInfo.categoryInfoList,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}
