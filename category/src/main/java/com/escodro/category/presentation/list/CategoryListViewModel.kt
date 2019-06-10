package com.escodro.category.presentation.list

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.DeleteCategory
import com.escodro.domain.usecase.LoadAllCategories
import com.escodro.domain.viewdata.ViewData
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentCategoryBinding].
 */
class CategoryListViewModel(
    private val loadCategoriesUseCase: LoadAllCategories,
    private val deleteCategoryUseCase: DeleteCategory
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<ViewData.Category>) -> Unit) {
        val disposable = loadCategoriesUseCase().subscribe { onListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given category.
     *
     * @param category category to be removed
     */
    fun deleteCategory(category: ViewData.Category) {
        val disposable = deleteCategoryUseCase(category).subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
