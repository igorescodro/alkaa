package com.escodro.alkaa.ui.category.create

/**
 * Delegate interface responsible to [NewCategoryFragment] UI operations.
 *
 * @author Igor Escodro on 3/6/18.
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
}
