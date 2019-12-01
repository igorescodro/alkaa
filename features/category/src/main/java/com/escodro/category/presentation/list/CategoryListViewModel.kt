package com.escodro.category.presentation.list

import androidx.lifecycle.ViewModel
import com.escodro.category.mapper.CategoryMapper
import com.escodro.category.model.Category
import com.escodro.domain.usecase.category.DeleteCategory
import com.escodro.domain.usecase.category.LoadAllCategories
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentCategoryBinding].
 */
internal class CategoryListViewModel(
    private val loadCategoriesUseCase: LoadAllCategories,
    private val deleteCategoryUseCase: DeleteCategory,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) {
        val disposable =
            loadCategoriesUseCase.test()
                .map { categoryMapper.fromDomain(it) }
                .subscribe {
                    onListLoaded(it)
                }
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given category.
     *
     * @param category category to be removed
     */
    fun deleteCategory(category: Category) {
        val disposable = deleteCategoryUseCase
            .test(categoryMapper.toDomain(category))
            .subscribe()

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
