package com.escodro.domain.usecase.task

import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.viewdata.ViewData
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import java.util.Calendar
import org.junit.Rule
import org.junit.Test

class CompleteTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<ViewData.Task>(relaxed = true)

    private val mockGetTask = mockk<GetTask>(relaxed = true)

    private val mockUpdateTask = mockk<UpdateTask>(relaxed = true)

    private val mockCalendar = mockk<TaskCalendar>(relaxed = true)

    private val completeTask = CompleteTask(mockGetTask, mockUpdateTask, mockCalendar)

    @Test
    fun `check if task was completed`() {
        val currentTime = Calendar.getInstance()

        every { mockCalendar.getCurrentCalendar() } returns currentTime
        every { mockGetTask.invoke(any()) } returns Single.just(mockTask)

        completeTask(mockTask.id).subscribe()

        verify { mockTask.completed = true }
        verify { mockTask.completedDate = currentTime }
        verify { mockUpdateTask.invoke(any()) }
    }
}
