package com.escodro.search.presentation

import com.escodro.search.model.TaskSearchItem
import kotlinx.collections.immutable.ImmutableList

/**
 * Represents the possible UI stated of Search screen.
 */
internal sealed class SearchViewState {

    /**
     * Represents the stated where the screen is loading.
     */
    internal data object Loading : SearchViewState()

    /**
     * Represents the stated where the tasks matches the query.
     */
    internal data class Loaded(val taskList: ImmutableList<TaskSearchItem>) : SearchViewState()

    /**
     * Represents the state where there are no tasks matching the query.
     */
    internal data object Empty : SearchViewState()
}
