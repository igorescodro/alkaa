package com.escodro.tracker.mapper

import com.escodro.designsystem.extensions.toArgbColor
import com.escodro.domain.model.TaskWithCategory
import com.escodro.tracker.model.Tracker
import kotlinx.collections.immutable.toImmutableList

/**
 * Maps Tracker between View and Domain.
 */
internal class TrackerMapper {

    /**
     * Maps from a grouped [Map] of [List<ViewData.TaskWithCategory>] to [Tracker].
     *
     * @param list list to be mapped
     *
     * @return the converted object
     */
    internal fun toTracker(list: List<TaskWithCategory>): Tracker.Info {
        val count = list.count()
        val categories = list.groupBy { task -> task.category?.id }.map { toCategory(it, count) }
        return Tracker.Info(categories.toImmutableList())
    }

    private fun toCategory(
        map: Map.Entry<Long?, List<TaskWithCategory>>,
        totalCount: Int,
    ): Tracker.CategoryInfo {
        val first = map.value.first()
        val taskCount = map.value.size
        return Tracker.CategoryInfo(
            name = first.category?.name,
            color = first.category?.color?.toArgbColor(),
            taskCount = taskCount,
            percentage = taskCount.toFloat() / totalCount,
        )
    }
}
