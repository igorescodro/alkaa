package com.escodro.domain.usecase.tracker.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.TaskWithCategoryRepository
import com.escodro.domain.usecase.tracker.LoadCompletedTasksByPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar

/**
 * Use case to get completed tasks in Tracker format for the last month from the database.
 */
internal class LoadCompletedTasksByPeriodImpl(
    private val repository: TaskWithCategoryRepository
) : LoadCompletedTasksByPeriod {

    /**
     * Gets completed tasks in Tracker format for the last month.
     */
    override operator fun invoke(): Flow<List<TaskWithCategory>> =
        repository.findAllTasksWithCategory()
            .map { list ->
                list.filter { item -> item.task.completed }
                    .filter(::filterByLastMonth)
            }

    private fun filterByLastMonth(task: TaskWithCategory): Boolean {
        val lastMonth = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, LAST_30_DAYS) }
        return task.task.completedDate?.after(lastMonth) ?: false
    }

    companion object {
        private const val LAST_30_DAYS = -30
    }
}
