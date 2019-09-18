package com.escodro.domain.usecase.tracker

import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.viewdata.ViewData
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.reactivex.Flowable
import io.reactivex.observers.TestObserver
import java.util.Calendar
import org.junit.Rule
import org.junit.Test

class LoadCompletedTrackerTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockCompletedTasks = mockk<LoadCompletedTasks>(relaxed = true)

    private val completeTracker = LoadCompletedTracker(mockCompletedTasks)

    @Test
    fun `check if completed tasks are shown in group`() {
        val category1 = ViewData.Category(1, "Category A", "#FFFFFF")
        val category2 = ViewData.Category(2, "Category B", "#CCCCCC")
        val category3 = ViewData.Category(3, "Category C", "#AAAAAA")

        val calendarIn = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -15) }
        val calendarOut = Calendar.getInstance().apply { add(Calendar.MONTH, -3) }

        val task1 = ViewData.Task(1, false, "Task 1")
        val task2 = ViewData.Task(2, true, "Task 2", completedDate = calendarIn)
        val task3 = ViewData.Task(3, true, "Task 3", completedDate = calendarIn)
        val task4 = ViewData.Task(4, false, "Task 4")
        val task5 = ViewData.Task(5, true, "Task 5", completedDate = calendarIn)
        val task6 = ViewData.Task(6, true, "Task 6", completedDate = calendarOut)

        val taskList = listOf(
            ViewData.TaskWithCategory(task1, category1),
            ViewData.TaskWithCategory(task2, category2),
            ViewData.TaskWithCategory(task3, category2),
            ViewData.TaskWithCategory(task4, category3),
            ViewData.TaskWithCategory(task5, category3),
            ViewData.TaskWithCategory(task6, category3)
        )

        val assertList = listOf(
            ViewData.Tracker(category2.name, category2.color, 2),
            ViewData.Tracker(category3.name, category3.color, 1)
        )

        every { mockCompletedTasks() } returns Flowable.just(taskList)

        val testObserver = TestObserver<List<ViewData.Tracker>>()
        completeTracker().subscribe(testObserver)
        testObserver.assertValue(assertList)
    }

    @Test
    fun `show tasks without category`() {
        val calendarIn = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -10) }
        val task = ViewData.Task(3, true, "Task 3", completedDate = calendarIn)

        val taskList = listOf(ViewData.TaskWithCategory(task, null))
        every { mockCompletedTasks() } returns Flowable.just(taskList)

        val testObserver = TestObserver<List<ViewData.Tracker>>()
        completeTracker().subscribe(testObserver)

        val assertList = listOf(ViewData.Tracker(null, null, 1))
        testObserver.assertValue(assertList)
    }
}
