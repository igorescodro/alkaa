package com.escodro.tracker.mapper

import com.escodro.domain.model.TaskWithCategory
import com.escodro.tracker.model.Tracker

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

        val categories = list.groupBy { task -> task.category?.id }
            .map(::toCategory)

        val count = categories.sumBy { tracker -> tracker.taskCount }
        return Tracker.Info(count, categories)
    }

    private fun toCategory(map: Map.Entry<Long?, List<TaskWithCategory>>):
        Tracker.Category {

        val first = map.value.first()
        return Tracker.Category(first.category?.name, first.category?.color, map.value.size)
    }
}
