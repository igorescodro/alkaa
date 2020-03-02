package com.escodro.search.presentation

import com.escodro.search.model.TaskSearch

/**
 * Represents the possible UI stated of [SearchFragment].
 */
internal sealed class SearchUIState {

    /**
     * Represents the stated where the tasks matches the query.
     */
    internal data class Loaded(val taskList: List<TaskSearch>) : SearchUIState()

    /**
     * Represents the state where there are no tasks matching the query.
     */
    internal object Empty : SearchUIState()
}
