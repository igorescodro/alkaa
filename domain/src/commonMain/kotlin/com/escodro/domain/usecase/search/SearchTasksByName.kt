package com.escodro.domain.usecase.search

import com.escodro.domain.model.TaskWithCategory
import kotlinx.coroutines.flow.Flow

/**
 * Use case to search for a specific task by name.
 */
interface SearchTasksByName {
    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    suspend operator fun invoke(query: String): Flow<List<TaskWithCategory>>
}
