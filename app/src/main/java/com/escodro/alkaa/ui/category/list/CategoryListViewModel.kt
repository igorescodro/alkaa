package com.escodro.alkaa.ui.category.list

import android.arch.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [com.escodro.alkaa.databinding
 * .FragmentCategoryBinding].
 *
 * Created by Igor Escodro on 5/3/18.
 */
class CategoryListViewModel(private val contract: CategoryListContract) :
    ViewModel() {

    var delegate: CategoryListDelegate? = null

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories() {
        val disposable = contract.loadCategories().subscribe { delegate?.updateList(it) }
        compositeDisposable.add(disposable)
    }

    /**
     * Deletes the given category.
     *
     * @param category category to be removed
     */
    fun deleteCategory(category: Category) {
        val disposable = contract.deleteTask(category)
            .doOnComplete { onCategoryRemoved(category) }
            .subscribe()
        compositeDisposable.add(disposable)
    }

    private fun onCategoryRemoved(category: Category) {
        delegate?.onTaskRemoved(category)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
        delegate = null
    }
}
