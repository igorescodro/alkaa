package com.escodro.search.presentation

import com.escodro.search.model.TaskSearchItem

/**
 * Represents the possible UI stated of Search screen.
 */
internal sealed class SearchUIState {

    /**
     * Represents the stated where the tasks matches the query.
     */
    internal data class Loaded(val taskList: List<TaskSearchItem>) : SearchUIState()

    /**
     * Represents the state where there are no tasks matching the query.
     */
    internal object Empty : SearchUIState()
}
