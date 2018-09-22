package com.escodro.alkaa.ui.category.list

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentCategoryBinding].
 *
 * Created by Igor Escodro on 5/3/18.
 */
class CategoryListViewModel(private val contract: CategoryListContract) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) {
        val disposable = contract.loadCategories().subscribe { onListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given category.
     *
     * @param category category to be removed
     */
    fun deleteCategory(category: Category, onCategoryRemoved: (category: Category) -> Unit) {
        val disposable = contract.deleteTask(category)
            .doOnComplete { onCategoryRemoved(category) }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
