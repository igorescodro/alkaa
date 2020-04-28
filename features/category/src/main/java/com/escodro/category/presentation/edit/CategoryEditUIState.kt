package com.escodro.category.presentation.edit

import com.escodro.category.model.Category

/**
 * Represents the possible UI stated of [CategoryEditFragment].
 */
internal sealed class CategoryEditUIState {

    /**
     * Represents the stated where the category is saved.
     */
    internal object Saved : CategoryEditUIState()

    /**
     * Represents the stated where the category has empty name.
     */
    internal object EmptyName : CategoryEditUIState()

    /**
     * Represents the stated where the category is loaded.
     */
    internal data class Loaded(val category: Category) : CategoryEditUIState()

    /**
     * Represents the stated where the category has any error.
     */
    internal object Error : CategoryEditUIState()
}
