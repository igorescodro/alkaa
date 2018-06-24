package com.escodro.alkaa.ui.category.list

import com.escodro.alkaa.data.local.model.Category

/**
 * Delegate interface responsible to [CategoryListFragment] UI operations.
 */
interface CategoryListDelegate {

    /**
     * Updates the [CategoryListAdapter] with the new list of [Category].
     *
     * @param list new list of [Category]
     */
    fun updateList(list: MutableList<Category>)
}
