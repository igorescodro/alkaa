package com.escodro.alkaa.ui.main

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [MainActivity].
 */
class MainViewModel(private val contract: MainContract) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) {
        val disposable = contract.loadCategories().subscribe { onListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
