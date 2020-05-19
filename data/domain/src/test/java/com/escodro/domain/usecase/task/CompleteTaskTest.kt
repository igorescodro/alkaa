package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.CalendarProviderFake
import com.escodro.domain.usecase.fake.NotificationInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class CompleteTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val alarmInteractor = AlarmInteractorFake()

    private val notificationInteractor = NotificationInteractorFake()

    private val calendarProvider = CalendarProviderFake()

    private val addTaskUseCase = AddTask(taskRepository)

    private val completeTaskUseCase =
        CompleteTask(taskRepository, alarmInteractor, notificationInteractor, calendarProvider)

    private val uncompleteTaskUseCase = UncompleteTask(taskRepository)

    private val updateTaskStatusUseCase =
        UpdateTaskStatus(taskRepository, completeTaskUseCase, uncompleteTaskUseCase)

    private val getTaskUseCase = GetTask(taskRepository)

    @Before
    fun setup() = runBlockingTest {
        taskRepository.cleanTable()
        alarmInteractor.clear()
        notificationInteractor.clear()
    }

    @Test
    fun `test if a task is updated as completed`() = runBlockingTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task)

        val result = getTaskUseCase(task.id).first()
        Assert.assertTrue(result.completed)
    }

    @Test
    fun `test if a task is updated as completed by id`() = runBlockingTest {
        val task = Task(id = 17, title = "change smartphone", completed = false)
        addTaskUseCase(task)
        completeTaskUseCase(task.id)

        val result = getTaskUseCase(task.id).first()
        Assert.assertTrue(result.completed)
    }

    @Test
    fun `test if a task is updated as uncompleted`() = runBlockingTest {
        val task = Task(id = 18, title = "buy soy milk", completed = false)
        addTaskUseCase(task)
        uncompleteTaskUseCase(task)

        val result = getTaskUseCase(task.id).first()
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

        val result1 = getTaskUseCase(task1.id).first()
        val result2 = getTaskUseCase(task2.id).first()

        Assert.assertFalse(result1.completed)
        Assert.assertTrue(result2.completed)
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
}
