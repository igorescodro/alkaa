package com.escodro.repository

import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.repository.SearchRepository
import com.escodro.repository.datasource.SearchDataSource
import com.escodro.repository.mapper.TaskWithCategoryMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
    private val mapper: TaskWithCategoryMapper
) : SearchRepository {

    override suspend fun findTaskByName(query: String): Flow<List<TaskWithCategory>> =
        searchDataSource.findTaskByName(query).map { mapper.toDomain(it) }
}
