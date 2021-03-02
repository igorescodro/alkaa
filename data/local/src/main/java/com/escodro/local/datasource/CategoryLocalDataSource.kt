package com.escodro.local.datasource

import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.model.Category
import javax.inject.Inject

/**
 * Local implementation of [CategoryDataSource].
 */
internal class CategoryLocalDataSource @Inject constructor(
    daoProvider: DaoProvider,
    private val categoryMapper: CategoryMapper
) : CategoryDataSource {

    private val categoryDao = daoProvider.getCategoryDao()

    override suspend fun insertCategory(category: Category) =
        categoryDao.insertCategory(categoryMapper.fromRepo(category))

    override suspend fun insertCategory(category: List<Category>) =
        categoryDao.insertCategory(categoryMapper.fromRepo(category))

    override suspend fun updateCategory(category: Category) =
        categoryDao.updateCategory(categoryMapper.fromRepo(category))

    override suspend fun deleteCategory(category: Category) =
        categoryDao.deleteCategory(categoryMapper.fromRepo(category))

    override suspend fun cleanTable() =
        categoryDao.cleanTable()

    override suspend fun findAllCategories(): List<Category> =
        categoryDao.findAllCategories().map { categoryMapper.toRepo(it) }

    override suspend fun findCategoryById(categoryId: Long): Category? =
        categoryDao.findCategoryById(categoryId)?.let { categoryMapper.toRepo(it) }
}
