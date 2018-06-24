package com.escodro.alkaa.ui.main

import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.ui.category.list.CategoryListAdapter

/**
 * Delegate interface responsible to [MainActivity] UI operations.
 */
interface MainDelegate {

    /**
     * Updates the [CategoryListAdapter] with the new list of [Category].
     *
     * @param list new list of [Category]
     */
    fun updateList(list: MutableList<Category>)
}
