package com.escodro.tracker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import com.escodro.domain.viewdata.ViewData
import com.escodro.tracker.model.mapper.TrackerMapper
import com.escodro.tracker.presentation.TrackerUIState
import com.escodro.tracker.presentation.TrackerViewModel
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TrackerViewModelTest {

    @get:Rule
    var instantTaskRule = InstantTaskExecutorRule()

    private val mockUseCase = mockk<LoadCompletedTasksByPeriod>(relaxed = true)

    private val mockObserver = mockk<Observer<TrackerUIState>>()

    private val viewModel = TrackerViewModel(mockUseCase, TrackerMapper())

    @Before
    fun setup() {
        RxJavaPlugins.setErrorHandler { /* Swallowed mock errors as said on the Rx Docs*/ }
        viewModel.viewState.observeForever(mockObserver)
    }

    @Test
    fun `check if show state was called`() {
        val obj1 = mockk<ViewData.TaskWithCategory>(relaxed = true)
        val obj2 = mockk<ViewData.TaskWithCategory>(relaxed = true)
        val mockList = listOf(obj1, obj2)

        every { mockUseCase.invoke() } returns Single.just(mockList)
        viewModel.loadData()

        verify(exactly = 1) { mockObserver.onChanged(any()) }
        assert(viewModel.viewState.value is TrackerUIState.ShowDataState)
    }

    @Test
    fun `check if empty state was called`() {
        every { mockUseCase.invoke() } returns Single.just(listOf())
        viewModel.loadData()

        verify(exactly = 1) { mockObserver.onChanged(any()) }
        assert(viewModel.viewState.value is TrackerUIState.EmptyChartState)
    }
}
