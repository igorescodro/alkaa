package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.test.ImmediateSchedulerRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UpdateTaskTest {

    @get:Rule
    var testSchedulerRule = ImmediateSchedulerRule()

    private val mockTask = mockk<Task>(relaxed = true)

    private val mockTaskRepo = mockk<TaskRepository>(relaxed = true)

    private val updateTask = UpdateTask(mockTaskRepo)

    private lateinit var testObserver: TestObserver<Completable>

    @Before
    fun setup() {
        testObserver = TestObserver()
    }

    @Test
    fun `check if repo function was called`() {
        every { mockTaskRepo.updateTask(mockTask) } returns Completable.complete()

        updateTask(mockTask).subscribe(testObserver)
        testObserver.assertComplete()

        verify { mockTaskRepo.updateTask(mockTask) }
    }
}
