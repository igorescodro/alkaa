package com.escodro.domain.usecase.task

import com.escodro.domain.model.Task
import com.escodro.domain.repository.TaskRepository
import io.reactivex.Single
import java.util.Calendar

/**
 * Use case to get all tasks scheduled in the future that are not completed from the database.
 */
class GetFutureTasks(private val taskRepository: TaskRepository) {

    /**
     * Gets all the uncompleted tasks in the future.
     *
     * @return observable to be subscribe
     */
    operator fun invoke(): Single<MutableList<Task>> =
        taskRepository.findAllTasksWithDueDate()
            .flattenAsObservable { it }
            .filter { isInFuture(it.dueDate) }
            .toList()

    private fun isInFuture(calendar: Calendar?): Boolean {
        val currentTime = Calendar.getInstance()
        return calendar?.after(currentTime) ?: false
    }
}
