package com.escodro.local.datasource

import com.escodro.local.mapper.CategoryMapper
import com.escodro.local.provider.DaoProvider
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.model.Category
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Local implementation of [CategoryDataSource].
 */
internal class CategoryLocalDataSource(
    daoProvider: DaoProvider,
    private val categoryMapper: CategoryMapper
) : CategoryDataSource {

    private val categoryDao = daoProvider.getCategoryDao()

    override fun insertCategory(category: Category): Completable =
        categoryDao.insertCategory(categoryMapper.fromRepo(category))

    override fun insertCategory(category: List<Category>): Completable =
        categoryDao.insertCategory(categoryMapper.fromRepo(category))

    override fun updateCategory(category: Category): Completable =
        categoryDao.updateCategory(categoryMapper.fromRepo(category))

    override fun deleteCategory(category: Category): Completable =
        categoryDao.deleteCategory(categoryMapper.fromRepo(category))

    override fun cleanTable(): Completable =
        categoryDao.cleanTable()

    override fun findAllCategories(): Flowable<List<Category>> =
        categoryDao.getAllCategories().map { categoryMapper.toRepo(it) }

    override fun findCategoryByName(name: String): Single<Category> =
        categoryDao.findCategoryByName(name).map { categoryMapper.toRepo(it) }

    override fun findCategoryById(categoryId: Long): Single<Category> =
        categoryDao.findCategory(categoryId).map { categoryMapper.toRepo(it) }
}
