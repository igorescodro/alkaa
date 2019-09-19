package com.escodro.tracker.presentation

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.tracker.LoadCompletedTracker

/**
 * [ViewModel] responsible to provide information to Task Tracker layout.
 */
class TrackerViewModel(private val loadTrackerUseCase: LoadCompletedTracker) : ViewModel()
