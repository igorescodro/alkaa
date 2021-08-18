package com.escodro.alarm

import android.content.Context
import com.escodro.alarm.model.Task
import com.escodro.alarm.notification.TaskNotificationScheduler
import com.escodro.core.extension.cancelAlarm
import com.escodro.core.extension.setExactAlarm
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class TaskNotificationSchedulerTest {

    private val mockContext = mockk<Context>(relaxed = true)

    private val mockTask = mockk<Task>(relaxed = true)

    private val scheduler = TaskNotificationScheduler(mockContext)

    @Before
    fun setUp() = mockkStatic("com.escodro.core.extension.extension-context")

    @Test
    fun `check if alarm was scheduled with valid task`() {
        scheduler.scheduleTaskAlarm(mockTask.id, mockTask.dueDate!!.time.time)
        verify { mockContext.setExactAlarm(mockTask.dueDate!!.time.time, any()) }
    }

    @Test
    fun `check if alarm is canceled with valid task`() {
        scheduler.cancelTaskAlarm(mockTask.id)
        verify { mockContext.cancelAlarm(any()) }
    }
}
