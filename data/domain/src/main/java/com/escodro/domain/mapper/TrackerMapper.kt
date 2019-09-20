package com.escodro.domain.mapper

import com.escodro.domain.viewdata.ViewData

/**
 * Converts between the [ViewData.TaskWithCategory] model from the database and [ViewData.Task] UI object.
 */
class TrackerMapper {

    /**
     * Maps from a grouped [Map] of [List<ViewData.TaskWithCategory>] to [ViewData.Tracker].
     *
     * @param map map to be mapped
     *
     * @return the converted object
     */
    fun toTracker(map: Map.Entry<Long?, List<ViewData.TaskWithCategory>>): ViewData.Tracker {
        val first = map.value.first()
        return ViewData.Tracker(first.category?.name, first.category?.color, map.value.size)
    }
}
