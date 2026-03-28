package com.escodro.domain.usecase.taskwithcategory

import com.escodro.domain.model.Task
import com.escodro.domain.model.TaskGroup
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.fake.DateTimeProviderFake
import com.escodro.domain.usecase.fake.TaskWithCategoryRepositoryFake
import com.escodro.domain.usecase.taskwithcategory.implementation.LoadCategoryTasksImpl
import com.escodro.test.rule.CoroutinesTestDispatcher
import com.escodro.test.rule.CoroutinesTestDispatcherImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class LoadCategoryTasksTest :
    CoroutinesTestDispatcher by CoroutinesTestDispatcherImpl() {

    private val repository = TaskWithCategoryRepositoryFake()
    private val dateProvider = DateTimeProviderFake()
    private val useCase = LoadCategoryTasksImpl(
        repository = repository,
        dateTimeProvider = dateProvider,
    )

    @BeforeTest
    fun setup() {
        repository.clear()
    }

    // Overdue = due date before 1993-04-15
    @Test
    fun `test if overdue tasks are grouped in the overdue section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(year = 1993, month = Month.APRIL, day = 14))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Overdue })
        assertEquals(expected = 1, actual = groups.filterIsInstance<TaskGroup.Overdue>().first().tasks.size)
    }

    @Test
    fun `test if tasks due today are grouped in the due today section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(year = 1993, month = Month.APRIL, day = 15))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.DueToday })
    }

    @Test
    fun `test if upcoming tasks are grouped in the upcoming section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = LocalDate(year = 1993, month = Month.APRIL, day = 16))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Upcoming })
    }

    @Test
    fun `test if tasks without due date are grouped in the no due date section`() = runTest {
        // Given
        val task = buildTask(id = 1L, dueDate = null)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.NoDueDate })
    }

    @Test
    fun `test if completed tasks are grouped in the completed section`() = runTest {
        // Given
        val task = buildTask(id = 1L, isCompleted = true)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Completed })
    }

    @Test
    fun `test if completed tasks with past due date are grouped in completed not overdue`() = runTest {
        // Given — task is both overdue AND completed; should go to Completed
        val overdueDate = LocalDate(year = 1993, month = Month.APRIL, day = 14)
        val task = buildTask(id = 1L, dueDate = overdueDate, isCompleted = true)
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then
        assertTrue(groups.any { it is TaskGroup.Completed })
        assertTrue(groups.none { it is TaskGroup.Overdue })
    }

    @Test
    fun `test if empty sections are not included in the result`() = runTest {
        // Given — one overdue task only
        val task = buildTask(id = 1L, dueDate = LocalDate(year = 1993, month = Month.APRIL, day = 14))
        repository.insertTaskWithCategory(buildTaskWithCategory(task))

        // When
        val groups = useCase(categoryId = 1L).first()

        // Then — only Overdue is emitted; other sections absent
        assertEquals(expected = 1, actual = groups.size)
        assertTrue(groups.first() is TaskGroup.Overdue)
    }

    // Helpers
    private fun buildTask(
        id: Long,
        dueDate: LocalDate? = null,
        isCompleted: Boolean = false,
    ) = Task(
        id = id,
        title = "Task $id",
        categoryId = 1L,
        isCompleted = isCompleted,
        dueDate = dueDate?.let { LocalDateTime(it, LocalTime(hour = 9, minute = 0)) },
    )

    private fun buildTaskWithCategory(task: Task) =
        TaskWithCategory(task = task, category = null)
}
