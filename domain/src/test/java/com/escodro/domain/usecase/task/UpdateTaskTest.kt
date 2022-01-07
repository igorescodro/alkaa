package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskCategoryImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskDescriptionImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskImpl
import com.escodro.domain.usecase.task.implementation.UpdateTaskTitleImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class UpdateTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val updateTaskUseCase = UpdateTaskImpl(taskRepository, glanceInteractor)

    private val loadTaskUseCase = LoadTaskImpl(taskRepository)

    private val updateTitleUseCase =
        UpdateTaskTitleImpl(loadTaskUseCase, updateTaskUseCase, glanceInteractor)

    private val updateDescriptionUseCase =
        UpdateTaskDescriptionImpl(loadTaskUseCase, updateTaskUseCase)

    private val updateTaskCategoryUseCase =
        UpdateTaskCategoryImpl(loadTaskUseCase, updateTaskUseCase)

    @Before
    fun setup() {
        glanceInteractor.clean()
    }

    @Test
    fun `test if task is updated`() = runBlockingTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val updatedTask = task.copy(title = "it's funny", description = "my mistake!")
        updateTaskUseCase(updatedTask)

        val loadedTask = loadTaskUseCase(task.id)
        Assert.assertEquals(updatedTask, loadedTask)
    }

    @Test
    fun `test if task title is updated`() = runBlockingTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val newTitle = "it's updated"
        updateTitleUseCase(task.id, newTitle)

        val loadedTask = loadTaskUseCase(task.id)
        Assert.assertEquals(newTitle, loadedTask!!.title)
    }

    @Test
    fun `test if task description is updated`() = runBlockingTest {
        val task = Task(id = 15, title = "its funny", description = "indeed")
        addTaskUseCase(task)

        val newDescription = "that's the way"
        updateDescriptionUseCase(task.id, newDescription)

        val loadedTask = loadTaskUseCase(task.id)
        Assert.assertEquals(newDescription, loadedTask!!.description)
    }

    @Test
    fun `test if task category is updated`() = runBlockingTest {
        val task = Task(id = 15, title = "its funny", categoryId = null)
        addTaskUseCase(task)

        val newCategory = 10L
        updateTaskCategoryUseCase(task.id, newCategory)

        val loadedTask = loadTaskUseCase(task.id)
        Assert.assertEquals(newCategory, loadedTask!!.categoryId)
    }

    @Test
    fun `test if the glance was notified`() = runBlockingTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        Assert.assertTrue(glanceInteractor.wasNotified)
    }
}
