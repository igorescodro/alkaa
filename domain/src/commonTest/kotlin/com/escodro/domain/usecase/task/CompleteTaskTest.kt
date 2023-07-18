package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.CalendarProviderFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskStatusImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

internal class CompleteTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val calendarProvider = CalendarProviderFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val completeTaskUseCase =
        CompleteTask(taskRepository, alarmInteractor, notificationInteractor, calendarProvider)

    private val uncompleteTaskUseCase = UncompleteTask(taskRepository)

    private val updateTaskStatusUseCase = UpdateTaskStatusImpl(
        taskRepository,
        glanceInteractor,
        completeTaskUseCase,
        uncompleteTaskUseCase,
    )

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
        notificationInteractor.clear()
    }

    @Test
    fun `test if a task is updated as completed`() = runTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        assertNotNull(result)
        assertTrue(result.completed)
    }

    @Test
    fun `test if a task is updated as completed by id`() = runTest {
        val task = Task(id = 17, title = "change smartphone", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task.id)

        val result = getTaskUseCase(task.id)

        require(result != null)
        assertTrue(result.completed)
    }

    @Test
    fun `test if a task is updated as uncompleted`() = runTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        uncompleteTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        require(result != null)
        assertFalse(result.completed)
    }

    @Test
    fun `test if the completed status is inverted`() = runTest {
        val task1 = Task(id = 99, title = "watch tech talk", completed = true)
        val task2 = Task(id = 88, title = "write paper", completed = false)

        addTaskUseCase(task1)
        addTaskUseCase(task2)

        updateTaskStatusUseCase(task1.id)
        updateTaskStatusUseCase(task2.id)

        val loadedTask1 = getTaskUseCase(task1.id)
        val loadedTask2 = getTaskUseCase(task2.id)

        require(loadedTask1 != null)
        require(loadedTask2 != null)
        assertFalse(loadedTask1.completed)
        assertTrue(loadedTask2.completed)
    }

    @Test
    fun `test if the alarm is canceled when the task is completed`() = runTest {
        val task = Task(id = 19, title = "sato's meeting", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if the notification is dismissed when the task is completed`() = runTest {
        val task = Task(id = 20, title = "scrum master dog", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        assertFalse(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if the glance was notified`() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        assertTrue(glanceInteractor.wasNotified)
    }
}
