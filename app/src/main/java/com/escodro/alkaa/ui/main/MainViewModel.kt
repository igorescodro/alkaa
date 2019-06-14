package com.escodro.alkaa.ui.main

import androidx.lifecycle.ViewModel
import com.escodro.domain.usecase.category.LoadAllCategories
import com.escodro.domain.viewdata.ViewData
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [MainActivity].
 */
class MainViewModel(private val loadAllCategories: LoadAllCategories) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<ViewData.Category>) -> Unit) {
        val disposable = loadAllCategories().subscribe { onListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
