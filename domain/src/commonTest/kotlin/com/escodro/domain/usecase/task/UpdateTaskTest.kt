package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.AlarmInteractorFake
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskCategoryImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskDescriptionImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskTitleImpl
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class UpdateTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val updateTaskUseCase = UpdateTaskImpl(taskRepository, glanceInteractor)

    private val loadTaskUseCase = LoadTaskImpl(taskRepository)

    private val alarmInteractor = AlarmInteractorFake()

    private val updateTitleUseCase =
        UpdateTaskTitleImpl(loadTaskUseCase, updateTaskUseCase, alarmInteractor, glanceInteractor)

    private val updateDescriptionUseCase =
        UpdateTaskDescriptionImpl(loadTaskUseCase, updateTaskUseCase)

    private val updateTaskCategoryUseCase =
        UpdateTaskCategoryImpl(loadTaskUseCase, updateTaskUseCase)

    @BeforeTest
    fun setup() {
        glanceInteractor.clean()
        alarmInteractor.clear()
    }

    @Test
    fun test_if_task_is_updated() = runTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val updatedTask = task.copy(title = "it's funny", description = "my mistake!")
        updateTaskUseCase(updatedTask)

        val loadedTask = loadTaskUseCase(task.id)
        assertEquals(updatedTask, loadedTask)
    }

    @Test
    fun test_if_task_title_is_updated() = runTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val newTitle = "it's updated"
        updateTitleUseCase(task.id, newTitle)

        val loadedTask = loadTaskUseCase(task.id)
        assertEquals(newTitle, loadedTask!!.title)
    }

    @Test
    fun test_when_task_title_is_updated_than_the_notification_was_updated() = runTest {
        val dueDate = LocalDateTime(2021, 10, 10, 10, 10)
        val task = Task(id = 15, title = "flora card", dueDate = dueDate)
        addTaskUseCase(task)

        val newTitle = "float card"
        updateTitleUseCase(task.id, newTitle)

        assertEquals(alarmInteractor.updatedTask, task.copy(title = newTitle))
    }

    @Test
    fun test_when_task_title_is_updated_without_due_date_than_the_notification_was_not_updated() =
        runTest {
            val task = Task(id = 15, title = "miss rat", dueDate = null)
            addTaskUseCase(task)

            val newTitle = "miss mouse"
            updateTitleUseCase(task.id, newTitle)

            assertEquals(alarmInteractor.updatedTask, null)
        }

    @Test
    fun test_if_task_description_is_updated() = runTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val newDescription = "that's the way"
        updateDescriptionUseCase(task.id, newDescription)

        val loadedTask = loadTaskUseCase(task.id)
        assertEquals(newDescription, loadedTask!!.description)
    }

    @Test
    fun test_if_task_category_is_updated() = runTest {
        val task = Task(id = 15, title = "its funny", categoryId = null)
        addTaskUseCase(task)

        val newCategory = 10L
        updateTaskCategoryUseCase(task.id, newCategory)

        val loadedTask = loadTaskUseCase(task.id)
        assertEquals(newCategory, loadedTask!!.categoryId)
    }

    @Test
    fun test_if_the_glance_was_notified() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        assertTrue(glanceInteractor.wasNotified)
    }
}
