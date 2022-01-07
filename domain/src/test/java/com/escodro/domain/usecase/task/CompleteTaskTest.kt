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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
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
        uncompleteTaskUseCase
    )

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        glanceInteractor.clean()
        notificationInteractor.clear()
    }

    @Test
    fun `test if a task is updated as completed`() = runBlockingTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        Assert.assertNotNull(result)
        Assert.assertTrue(result?.completed == true)
    }

    @Test
    fun `test if a task is updated as completed by id`() = runBlockingTest {
        val task = Task(id = 17, title = "change smartphone", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task.id)

        val result = getTaskUseCase(task.id)

        require(result != null)
        Assert.assertTrue(result.completed)
    }

    @Test
    fun `test if a task is updated as uncompleted`() = runBlockingTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        uncompleteTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        require(result != null)
        Assert.assertFalse(result.completed)
    }

    @Test
    fun `test if the completed status is inverted`() = runBlockingTest {
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
        Assert.assertFalse(loadedTask1.completed)
        Assert.assertTrue(loadedTask2.completed)
    }

    @Test
    fun `test if the alarm is canceled when the task is completed`() = runBlockingTest {
        val task = Task(id = 19, title = "sato's meeting", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        Assert.assertFalse(alarmInteractor.isAlarmScheduled(task.id))
    }

    @Test
    fun `test if the notification is dismissed when the task is completed`() = runBlockingTest {
        val task = Task(id = 20, title = "scrum master dog", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        Assert.assertFalse(notificationInteractor.isNotificationShown(task.id))
    }

    @Test
    fun `test if the glance was notified`() = runBlockingTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        Assert.assertTrue(glanceInteractor.wasNotified)
    }
}
