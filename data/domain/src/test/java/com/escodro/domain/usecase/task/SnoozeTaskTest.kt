package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import java.lang.IllegalArgumentException
import java.util.Calendar
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SnoozeTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val snoozeTask = SnoozeTask(mockTaskRepo)

    private lateinit var testObserver: TestObserver<Completable>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun `check if task was snooze with positive number`() {
        every { mockTaskRepo.findTaskById(mockTask.id) } returns Single.just(mockTask)
        every { mockTaskRepo.updateTask(any()) } returns Completable.complete()

        val snoozeTime = 15

        snoozeTask(mockTask.id, snoozeTime).subscribe(testObserver)
        testObserver.assertComplete()

        val snoozedCalendar = mockTask.dueDate?.apply { add(Calendar.MINUTE, snoozeTime) }
        val updatedTask = mockTask.copy(dueDate = snoozedCalendar)

        verify { mockTaskRepo.updateTask(updatedTask) }
    }

    @Test
    fun `check if task was not snooze with negative number`() {
        every { mockTaskRepo.findTaskById(mockTask.id) } returns Single.just(mockTask)

        val snoozeTime = -15

        snoozeTask(mockTask.id, snoozeTime).subscribe(testObserver)
        testObserver.assertError(IllegalArgumentException::class.java)

        verify(exactly = 0) { mockTaskRepo.updateTask(any()) }
    }
}
