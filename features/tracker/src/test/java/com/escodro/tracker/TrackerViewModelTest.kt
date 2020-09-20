package com.escodro.tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import com.escodro.test.CoroutineTestRule
import com.escodro.tracker.mapper.TrackerMapper
import com.escodro.tracker.presentation.TrackerUIState
import com.escodro.tracker.presentation.TrackerViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.Rule
import org.junit.Test

class TrackerViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockUseCase = mockk<LoadCompletedTasksByPeriod>(relaxed = true)

    private val viewModel = TrackerViewModel(mockUseCase, TrackerMapper())

    @Test
    fun `check if show state was called`() {
        val obj1 = mockk<TaskWithCategory>(relaxed = true)
        val obj2 = mockk<TaskWithCategory>(relaxed = true)
        val mockList = listOf(obj1, obj2)

        every { mockUseCase.invoke() } returns flow { emit(mockList) }
        viewModel.loadData()

        assert(viewModel.viewState.value is TrackerUIState.ShowDataState)
    }

    @Test
    fun `check if empty state was called`() {
        every { mockUseCase.invoke() } returns flow { emit(listOf<TaskWithCategory>()) }
        viewModel.loadData()

        assert(viewModel.viewState.value is TrackerUIState.EmptyChartState)
    }
}
