package com.escodro.alkaa.ui.main

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [MainActivity].
 */
class MainViewModel(private val contract: MainContract) : ViewModel() {

    var delegate: MainDelegate? = null

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories() {
        val disposable = contract.loadCategories().subscribe { delegate?.updateList(it) }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
        delegate = null
    }
}
