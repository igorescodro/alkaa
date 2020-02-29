package com.escodro.domain.usecase.search

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository

/**
 * Use case to search for a specific task by name.
 */
class SearchTasksByName(private val searchRepository: SearchRepository) {

    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    suspend operator fun invoke(query: String): List<TaskWithCategory> =
        searchRepository.findTaskByName(query)
}
