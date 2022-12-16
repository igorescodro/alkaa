package com.escodro.tracker.model

import kotlinx.collections.immutable.ImmutableList

/**
 * UI representation of Tracker.
 */
internal sealed class Tracker {

    /**
     * UI Representation of the Tracker information.
     */
    internal data class Info(val categoryInfoList: ImmutableList<CategoryInfo>)

    /**
     * UI representation of each item of Tracker information.
     */
    internal data class CategoryInfo(
        val name: String?,
        val color: Int?,
        val taskCount: Int,
        val percentage: Float,
    )
}
