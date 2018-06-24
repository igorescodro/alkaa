package com.escodro.alkaa.ui.category.create

/**
 * Delegate interface responsible to [NewCategoryFragment] UI operations.
 */
interface NewCategoryDelegate {

    /**
     * Called when a category without description tries to be added.
     */
    fun onEmptyField()

    /**
     * Called when a new category is added.
     */
    fun onNewCategoryAdded()

    /**
     * Gets the category color.
     */
    fun getCategoryColor(): String?
}
