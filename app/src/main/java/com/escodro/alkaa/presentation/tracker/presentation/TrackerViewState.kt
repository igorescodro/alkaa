package com.escodro.alkaa.presentation.tracker.presentation

import com.escodro.alkaa.presentation.tracker.model.Tracker

/**
 * Represents the possible UI States of Tracker screen.
 */
internal sealed class TrackerViewState {

    /**
     * Represents the state where they are [Tracker.Info] to be shown on the screen.
     */
    internal data class Loaded(val trackerInfo: Tracker.Info) : TrackerViewState()

    /**
     * Represents the state where they are no information to be shown.
     */
    internal object Empty : TrackerViewState()

    /**
     * Represents the state where an error occurred.
     */
    internal data class Error(val cause: Throwable) : TrackerViewState()
}