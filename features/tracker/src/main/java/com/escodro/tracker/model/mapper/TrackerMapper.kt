package com.escodro.tracker.model.mapper

import com.escodro.domain.viewdata.ViewData
import com.escodro.tracker.model.Tracker

/**
 * Converts between the [ViewData.TaskWithCategory] model from the database and [Tracker] UI object.
 */
internal class TrackerMapper {

    /**
     * Maps from a grouped [Map] of [List<ViewData.TaskWithCategory>] to [Tracker].
     *
     * @param list list to be mapped
     *
     * @return the converted object
     */
    internal fun toTracker(list: List<ViewData.TaskWithCategory>): Tracker.Info {

        val categories = list.groupBy { task -> task.category?.id }
            .map(::toCategory)

        val count = categories.sumBy { tracker -> tracker.taskCount }
        return Tracker.Info(count, categories)
    }

    private fun toCategory(map: Map.Entry<Long?, List<ViewData.TaskWithCategory>>):
        Tracker.Category {

        val first = map.value.first()
        return Tracker.Category(first.category?.name, first.category?.color, map.value.size)
    }
}
