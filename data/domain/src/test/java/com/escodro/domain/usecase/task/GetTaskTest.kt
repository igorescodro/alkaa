package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val getTask = GetTask(mockTaskRepo)

    private lateinit var testObserver: TestObserver<Task>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun `check if repo function was called`() {
        every { mockTaskRepo.findTaskById(mockTask.id) } returns Single.just(mockTask)

        getTask(mockTask.id).subscribe(testObserver)
        testObserver.assertComplete()

        verify { mockTaskRepo.findTaskById(mockTask.id) }
    }
}
