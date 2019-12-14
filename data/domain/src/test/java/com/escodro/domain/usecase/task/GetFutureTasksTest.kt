package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import java.util.Calendar
import org.junit.Rule
import org.junit.Test

class GetFutureTasksTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockRepo = mockk<TaskRepository>(relaxed = true)

    private val getFutureTasks = GetFutureTasks(mockRepo)

    @Test
    fun `check if only tasks in the future are shown`() {
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, title = "Title", dueDate = futureCalendar)
        val task2 = Task(id = 2, title = "Title")
        val task3 = Task(id = 3, title = "Title", dueDate = futureCalendar)
        val task4 = Task(id = 4, title = "Title")
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)

        val repoList = listOf(task1, task2, task3, task4, task5)
        val assertList = listOf(task1, task3, task5)

        every { mockRepo.findAllTasksWithDueDate() } returns Single.just(repoList)

        val testObserver = TestObserver<List<Task>>()
        getFutureTasks().subscribe(testObserver)
        testObserver.assertValue(assertList)
    }

    @Test
    fun `check if completed tasks are ignored`() {
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, completed = true, title = "Title", dueDate = futureCalendar)
        val task2 = Task(id = 2, completed = true, title = "Title", dueDate = futureCalendar)
        val task3 = Task(id = 3, completed = true, title = "Title", dueDate = futureCalendar)
        val task4 = Task(id = 4, completed = false, title = "Title", dueDate = futureCalendar)
        val task5 = Task(id = 5, completed = false, title = "Title", dueDate = futureCalendar)

        val repoList = listOf(task1, task2, task3, task4, task5)
        val assertList = listOf(task4, task5)

        every { mockRepo.findAllTasksWithDueDate() } returns Single.just(repoList)

        val testObserver = TestObserver<List<Task>>()
        getFutureTasks().subscribe(testObserver)
        testObserver.assertValue(assertList)
    }

    @Test
    fun `check if uncompleted tasks on the past are ignored`() {
        val pastCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -15) }
        val futureCalendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 15) }

        val task1 = Task(id = 1, title = "Title", dueDate = pastCalendar)
        val task2 = Task(id = 2, title = "Title", dueDate = pastCalendar)
        val task3 = Task(id = 3, title = "Title", dueDate = pastCalendar)
        val task4 = Task(id = 4, title = "Title", dueDate = pastCalendar)
        val task5 = Task(id = 5, title = "Title", dueDate = futureCalendar)

        val repoList = listOf(task1, task2, task3, task4, task5)
        val assertList = listOf(task5)

        every { mockRepo.findAllTasksWithDueDate() } returns Single.just(repoList)

        val testObserver = TestObserver<List<Task>>()
        getFutureTasks().subscribe(testObserver)
        testObserver.assertValue(assertList)
    }
}
