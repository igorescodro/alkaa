package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.usecase.fake.GlanceInteractorFake
import com.escodro.domain.usecase.fake.TaskRepositoryFake
import com.escodro.domain.usecase.task.implementation.AddTaskImpl
import com.escodro.domain.usecase.task.implementation.LoadTaskImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

internal class AddTaskTest {

    private val taskRepository = TaskRepositoryFake()

    private val glanceInteractor = GlanceInteractorFake()

    private val addTaskUseCase = AddTaskImpl(taskRepository, glanceInteractor)

    private val getTaskUseCase = LoadTaskImpl(taskRepository)

    @BeforeTest
    fun setup() = runTest {
        taskRepository.cleanTable()
        glanceInteractor.clean()
    }

    @Test
    fun test_if_task_is_correctly_added() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        require(result != null)
        assertEquals(task, result)
    }

    @Test
    fun test_if_task_with_empty_title_is_not_added() = runTest {
        val task = Task(id = 15, title = " ", description = "this desc")
        addTaskUseCase(task)

        val result = getTaskUseCase(task.id)

        assertNull(result)
    }

    @Test
    fun test_if_the_glance_was_notified() = runTest {
        val task = Task(id = 15, title = "this title", description = "this desc")
        addTaskUseCase(task)

        assertTrue(glanceInteractor.wasNotified)
    }
}
