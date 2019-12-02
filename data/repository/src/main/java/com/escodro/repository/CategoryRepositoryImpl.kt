package com.escodro.repository

import com.escodro.domain.model.Category
import com.escodro.domain.repository.CategoryRepository
import com.escodro.repository.datasource.CategoryDataSource
import com.escodro.repository.mapper.CategoryMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

internal class CategoryRepositoryImpl(
    private val categoryDataSource: CategoryDataSource,
    private val categoryMapper: CategoryMapper
) : CategoryRepository {

    override fun insertCategory(category: Category): Completable =
        categoryDataSource.insertCategory(categoryMapper.toRepo(category))

    override fun insertCategory(category: List<Category>): Completable =
        categoryDataSource.insertCategory(categoryMapper.toRepo(category))

    override fun updateCategory(category: Category): Completable =
        categoryDataSource.updateCategory(categoryMapper.toRepo(category))

    override fun deleteCategory(category: Category): Completable =
        categoryDataSource.deleteCategory(categoryMapper.toRepo(category))

    override fun cleanTable(): Completable =
        categoryDataSource.cleanTable()

    override fun findAllCategories(): Flowable<List<Category>> =
        categoryDataSource.findAllCategories().map { categoryMapper.toDomain(it) }

    override fun findCategoryByName(name: String): Single<Category> =
        categoryDataSource.findCategoryByName(name).map { categoryMapper.toDomain(it) }

    override fun findCategoryById(categoryId: Long): Single<Category> =
        categoryDataSource.findCategoryById(categoryId).map { categoryMapper.toDomain(it) }
}
