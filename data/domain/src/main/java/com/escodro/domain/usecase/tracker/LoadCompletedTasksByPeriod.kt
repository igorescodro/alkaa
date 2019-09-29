package com.escodro.domain.usecase.tracker

import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.viewdata.ViewData
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Calendar

/**
 * Use case to get completed tasks in Tracker format for the last month from the database.
 */
class LoadCompletedTasksByPeriod(
    private val loadCompletedTasks: LoadCompletedTasks
) {

    /**
     * Gets completed tasks in Tracker format for the last month.
     */
    operator fun invoke(): Single<List<ViewData.TaskWithCategory>> =
        loadCompletedTasks()
            .flatMap {
                Flowable.fromIterable(it)
                    .filter { taskWithCategory -> filterByLastMonth(taskWithCategory) }
                    .toList()
                    .toFlowable()
            }.firstOrError()

    private fun filterByLastMonth(task: ViewData.TaskWithCategory): Boolean {
        val lastMonth = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, LAST_30_DAYS) }
        return task.task.completedDate?.after(lastMonth) ?: false
    }

    companion object {
        private const val LAST_30_DAYS = -30
    }
}
