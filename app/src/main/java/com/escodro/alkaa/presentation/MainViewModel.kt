package com.escodro.alkaa.presentation

import androidx.lifecycle.ViewModel
import com.escodro.alkaa.mapper.CategoryMapper
import com.escodro.alkaa.model.Category
import com.escodro.domain.usecase.category.LoadAllCategories
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [MainActivity].
 */
internal class MainViewModel(
    private val loadAllCategories: LoadAllCategories,
    private val categoryMapper: CategoryMapper
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads all categories.
     */
    fun loadCategories(onListLoaded: (list: List<Category>) -> Unit) {
        val disposable =
            loadAllCategories().map { categoryMapper.toView(it) }
                .subscribe { onListLoaded(it) }
        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
