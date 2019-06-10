package com.escodro.category.presentation.detail

import android.text.TextUtils
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.category.common.toStringColor
import com.escodro.domain.usecase.LoadCategory
import com.escodro.domain.usecase.SaveCategory
import com.escodro.domain.viewdata.ViewData
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to [CategoryDetailFragment].
 */
class CategoryDetailViewModel(
    private val loadCategory: LoadCategory,
    private val saveCategory: SaveCategory
) : ViewModel() {

    val newCategory = MediatorLiveData<String>()

    private val categoryData = MutableLiveData<ViewData.Category>()

    private val compositeDisposable = CompositeDisposable()

    init {
        newCategory.addSource(categoryData) { newCategory.value = it.name }
    }

    /**
     * Loads the categoryId based on the given id.
     *
     * @param categoryId categoryId id
     */
    fun loadCategory(categoryId: Long, onLoadCategory: (color: String) -> Unit) {
        val disposable = loadCategory.invoke(categoryId).subscribe(
            { category ->
                categoryData.value = category
                category.color?.let { color -> onLoadCategory(color) }
            },
            { Timber.e("Category not found in database") })

        compositeDisposable.add(disposable)
    }

    /**
     * Adds or updates a category.
     */
    fun saveCategory(
        onEmptyField: () -> Unit,
        getCategoryColor: () -> Int?,
        onCategoryAdded: () -> Unit
    ) {
        val name = newCategory.value
        val color = getCategoryColor()?.toStringColor() ?: return

        if (name == null || TextUtils.isEmpty(name)) {
            onEmptyField()
            return
        }

        val category = if (isEditMode()) {
            getCurrentCategory(name, color)
        } else {
            getNewCategory(name, color)
        }

        val disposable = saveCategory.invoke(category)
            .doOnComplete { onCategoryAdded() }
            .subscribe()

        compositeDisposable.add(disposable)
    }

    private fun isEditMode() = categoryData.value != null

    private fun getNewCategory(name: String, color: String) =
        ViewData.Category(name = name, color = color)

    private fun getCurrentCategory(name: String, color: String) =
        categoryData.apply {
            value?.name = name
            value?.color = color
        }.value ?: getNewCategory(name, color)

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
