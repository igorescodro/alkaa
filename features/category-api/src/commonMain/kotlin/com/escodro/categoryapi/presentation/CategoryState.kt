package com.escodro.categoryapi.presentation

import com.escodro.categoryapi.model.Category
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents the states of [CategoryListViewModel].
 */
sealed class CategoryState {

    /**
     * Loading state.
     */
    object Loading : CategoryState()

    /**
     * Loaded state.
     *
     * @property categoryList the loaded category list
     */
    data class Loaded(val categoryList: ImmutableList<Category>) : CategoryState()

    /**
     * Empty state, there are no categories to be shown.
     */
    object Empty : CategoryState()
}
