package com.escodro.tracker.model

/**
 * UI representation of Tracker.
 */
sealed class Tracker {

    /**
     * UI Representation of the Tracker information.
     */
    data class Info(val totalCount: Int, val categoryList: List<Category>)

    /**
     * UI representation of each item of Tracker information.
     */
    data class Category(val categoryName: String?, val categoryColor: String?, val taskCount: Int)
}
