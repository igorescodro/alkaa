package com.escodro.repository

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.mapper.TaskWithCategoryMapper

internal class SearchRepositoryImpl(
    private val searchDataSource: SearchDataSource,
    private val mapper: TaskWithCategoryMapper
) : SearchRepository {

    override suspend fun findTaskByName(query: String): List<TaskWithCategory> =
        mapper.toDomain(searchDataSource.findTaskByName(query))
}
