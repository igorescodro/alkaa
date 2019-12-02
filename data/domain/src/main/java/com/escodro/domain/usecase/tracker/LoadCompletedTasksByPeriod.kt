package com.escodro.domain.usecase.tracker

import com.escodro.core.extension.applySchedulers
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Calendar

/**
 * Use case to get completed tasks in Tracker format for the last month from the database.
 */
class LoadCompletedTasksByPeriod(private val repository: TaskWithCategoryRepository) {

    /**
     * Gets completed tasks in Tracker format for the last month.
     */
    operator fun invoke(): Single<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory(isCompleted = true)
            .flatMap {
                Flowable.fromIterable(it)
                    .filter(::filterByLastMonth)
                    .toList()
                    .toFlowable()
            }.firstOrError()
            .applySchedulers()

    private fun filterByLastMonth(task: TaskWithCategory): Boolean {
        val lastMonth = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, LAST_30_DAYS) }
        return task.task.completedDate?.after(lastMonth) ?: false
    }

    companion object {
        private const val LAST_30_DAYS = -30
    }
}
