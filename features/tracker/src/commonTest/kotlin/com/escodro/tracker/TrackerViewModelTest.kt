package com.escodro.tracker

import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import com.escodro.tracker.fake.LoadCompletedTasksByPeriodFake
import com.escodro.tracker.mapper.TrackerMapper
import com.escodro.tracker.presentation.TrackerViewModel
import com.escodro.tracker.presentation.TrackerViewState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class TrackerViewModelTest : CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val loadCompletedTasksByPeriod = LoadCompletedTasksByPeriodFake()

    private val viewModel = TrackerViewModel(loadCompletedTasksByPeriod, TrackerMapper())

    @BeforeTest
    fun setup() {
        loadCompletedTasksByPeriod.clean()
    }

    @Test
    fun `check if show state was called`() = runTest {
        // Given the use case returns the list with completed tasks by period
        loadCompletedTasksByPeriod.returnDefaultValues()
        val flow = viewModel.loadTracker()

        // When the latest event is collected
        val state = flow.first()

        // Then the state is loaded
        assertTrue(state is TrackerViewState.Loaded)
    }

    @Test
    fun `check if empty state was called`() = runTest {
        // Given the use case returns an empty list with completed tasks by period
        loadCompletedTasksByPeriod.clearList()
        val flow = viewModel.loadTracker()

        // When the latest event is collected
        val state = flow.first()

        // Then the state is empty
        assertTrue(state is TrackerViewState.Empty)
    }

    @Test
    fun `check if error state was called`() = runTest {
        // Given the use case returns an empty list with completed tasks by period
        loadCompletedTasksByPeriod.isErrorThrown = true
        val flow = viewModel.loadTracker()

        // When the latest event is collected
        val state = flow.first()

        // Then the state is empty
        assertTrue(state is TrackerViewState.Error)
    }
}
