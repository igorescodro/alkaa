package com.escodro.alkaa.ui.task.detail

import com.escodro.alkaa.data.local.model.Category

/**
 * Delegate interface responsible to [TaskDetailFragment] UI operations.
 */
interface TaskDetailDelegate {

    /**
     * Updates the [com.escodro.alkaa.common.view.ScrollableRadioGroup] with the new list of
     * [Category].
     *
     * @param list new list of [Category]
     */
    fun updateCategoryList(list: MutableList<Category>)
}
