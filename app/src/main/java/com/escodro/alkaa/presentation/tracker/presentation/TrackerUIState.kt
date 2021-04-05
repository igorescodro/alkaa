package com.escodro.alkaa.presentation.tracker.presentation

import com.escodro.alkaa.presentation.tracker.model.Tracker

/**
 * Represents the possible UI States of Tracker screen.
 */
internal sealed class TrackerUIState {

    /**
     * Represents the state where they are [Tracker.Info] to be shown on the screen.
     */
    internal data class Loaded(val trackerInfo: Tracker.Info) : TrackerUIState()

    /**
     * Represents the state where they are no information to be shown.
     */
    internal object Empty : TrackerUIState()
}
