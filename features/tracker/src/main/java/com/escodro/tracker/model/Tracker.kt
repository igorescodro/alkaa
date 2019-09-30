package com.escodro.tracker.model

/**
 * UI representation of Tracker.
 */
internal sealed class Tracker {

    /**
     * UI Representation of the Tracker information.
     */
    internal data class Info(val totalCount: Int, val categoryList: List<Category>)

    /**
     * UI representation of each item of Tracker information.
     */
    internal data class Category(val categoryName: String?, val categoryColor: String?, val taskCount: Int)
}
