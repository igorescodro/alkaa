package com.escodro.search.presentation

import com.escodro.search.model.TaskSearchItem

/**
 * Represents the possible UI stated of Search screen.
 */
internal sealed class SearchViewState {

    /**
     * Represents the stated where the screen is loading.
     */
    internal object Loading : SearchViewState()

    /**
     * Represents the stated where the tasks matches the query.
     */
    internal data class Loaded(val taskList: List<TaskSearchItem>) : SearchViewState()

    /**
     * Represents the state where there are no tasks matching the query.
     */
    internal object Empty : SearchViewState()
}
