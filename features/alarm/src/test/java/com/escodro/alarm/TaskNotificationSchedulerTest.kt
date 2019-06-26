package com.escodro.alarm

import android.content.Context
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.core.extension.cancelAlarm
import com.escodro.core.extension.setAlarm
import com.escodro.domain.viewdata.ViewData
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TaskNotificationSchedulerTest {

    private val mockContext = mockk<Context>(relaxed = true)

    private val mockTask = mockk<ViewData.Task>(relaxed = true)

    private val scheduler = TaskNotificationScheduler(mockContext)

    @Before
    fun setUp() = mockkStatic("com.escodro.core.extension.extension-context")

    @Test
    fun `check if alarm was scheduled with valid task`() {
        scheduler.scheduleTaskAlarm(mockTask)
        verify { mockContext.setAlarm(mockTask.dueDate!!.time!!.time, any()) }
    }

    @Test
    fun `check if alarm is not scheduled with null calendar`() {
        every { mockTask.dueDate } returns null

        scheduler.scheduleTaskAlarm(mockTask)
        verify(exactly = 0) { mockContext.setAlarm(any(), any()) }
    }

    @Test
    fun `check if alarm is canceled with valid task`() {
        scheduler.cancelTaskAlarm(mockTask.id)
        verify { mockContext.cancelAlarm(any()) }
    }
}
