package com.escodro.alkaa.ui.category.list

import android.arch.lifecycle.ViewModel
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
        compositeDisposable.add(
            contract.loadCategories().subscribe { delegate?.updateList(it) }
        )
    }
}
