package com.escodro.category.presentation.add

/**
 * Represents the possible UI stated of [CategoryAddFragment].
 */
internal sealed class CategoryAddUIState {

    /**
     * Represents the stated where the category is saved.
     */
    internal object Saved : CategoryAddUIState()

    /**
     * Represents the stated where the category has empty name.
     */
    internal object EmptyName : CategoryAddUIState()
}
