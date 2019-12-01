package com.escodro.domain.usecase.task

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.calendar.TaskCalendar
import com.escodro.domain.mapper.TaskMapper
import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import com.escodro.domain.viewdata.ViewData
import com.escodro.local.provider.DaoProvider
import io.reactivex.Completable

/**
 * Use case to add a task from the database.
 */
class AddTask(
    private val taskRepository: TaskRepository,
    private val daoProvider: DaoProvider,
    private val mapper: TaskMapper,
    private val taskCalendar: TaskCalendar
) {

    /**
     * Adds a task.
     *
     * @param task the task to be added
     *
     * @return observable to be subscribe
     */
    operator fun invoke(task: ViewData.Task): Completable {
        val entityTask = mapper.toEntityTask(task)
        entityTask.creationDate = taskCalendar.getCurrentCalendar()
        return daoProvider.getTaskDao().insertTask(entityTask).applySchedulers()
    }

    @Suppress("UndocumentedPublicFunction")
    fun test(task: Task): Completable =
        taskRepository.insertTask(task).applySchedulers()
}
