package com.escodro.domain.usecase.search.implementation

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository
import com.escodro.domain.usecase.search.SearchTasksByName

internal class SearchTasksByNameImpl(
    private val searchRepository: SearchRepository
) : SearchTasksByName {

    /**
     * Gets tasks based on the given name.
     *
     * @param query the name to query
     *
     * @return the list of tasks that match the given query
     */
    override suspend operator fun invoke(query: String): List<TaskWithCategory> =
        searchRepository.findTaskByName(query)
}
