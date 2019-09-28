package com.escodro.tracker.presentation

import com.escodro.domain.viewdata.ViewData

/**
 * Represents the possible UI States of [TrackerFragment].
 */
sealed class TrackerUIState {

    /**
     * Represents the state where they are [ViewData.Tracker] to be shown on the screen.
     */
    data class ShowDataState(val trackerList: List<ViewData.Tracker>, val taskCount: Int) :
        TrackerUIState()

    /**
     * Represents the state where they are no information to be shown.
     */
    object EmptyChartState : TrackerUIState()
}
