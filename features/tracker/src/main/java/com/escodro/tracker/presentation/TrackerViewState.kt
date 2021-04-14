package com.escodro.tracker.presentation

import com.escodro.tracker.model.Tracker

/**
 * Represents the possible UI States of Tracker screen.
 */
internal sealed class TrackerViewState {

    /**
     * Represents the stated where the screen is loading.
     */
    internal object Loading : TrackerViewState()

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
