package com.escodro.local.datasource

import com.escodro.local.dao.CategoryDao
import com.escodro.local.mapper.CategoryMapper
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Local implementation of [CategoryDataSource].
 */
internal class CategoryLocalDataSource(
    private val categoryDao: CategoryDao,
    private val categoryMapper: CategoryMapper,
) : CategoryDataSource {

    override suspend fun insertCategory(category: Category) =
        categoryDao.insertCategory(categoryMapper.fromRepo(category))

    override suspend fun updateCategory(category: Category) =
        categoryDao.updateCategory(categoryMapper.fromRepo(category))

    override suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(categoryMapper.fromRepo(category))

    override suspend fun cleanTable() =
        categoryDao.cleanTable()

    override fun findAllCategories(): Flow<List<Category>> =
        categoryDao.findAllCategories().map { categoryMapper.toRepo(it) }

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryDao.findCategoryById(categoryId)?.let { categoryMapper.toRepo(it) }
}
