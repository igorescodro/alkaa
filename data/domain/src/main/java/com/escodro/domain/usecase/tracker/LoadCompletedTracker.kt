package com.escodro.domain.usecase.tracker

import com.escodro.domain.usecase.taskwithcategory.LoadCompletedTasks
import com.escodro.domain.viewdata.ViewData
import io.reactivex.Flowable
import io.reactivex.Single
import java.util.Calendar

/**
 * Use case to get completed tasks in Tracker format for the last month from the database.
 */
class LoadCompletedTracker(private val loadCompletedTasks: LoadCompletedTasks) {

    /**
     * Gets completed tasks in Tracker format for the last month.
     */
    operator fun invoke(): Single<List<ViewData.Tracker>> =
        loadCompletedTasks()
            .flatMap {
                Flowable.fromIterable(it)
                    .filter { taskWithCategory -> filterByLastMonth(taskWithCategory) }
                    .toList()
                    .map { taskList -> mapToTrackerList(taskList) }
                    .toFlowable()
            }.firstOrError()

    private fun filterByLastMonth(task: ViewData.TaskWithCategory): Boolean {
        val lastMonth = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, LAST_30_DAYS) }
        return task.task.completedDate?.after(lastMonth) ?: false
    }

    private fun mapToTrackerList(taskList: List<ViewData.TaskWithCategory>): List<ViewData.Tracker> =
        taskList
            .groupBy { task -> task.category?.id }
            .map { map ->
                val first = map.value.first()
                ViewData.Tracker(first.category?.name, first.category?.color, map.value.size)
            }

    companion object {
        private const val LAST_30_DAYS = -30
    }
}
