package com.escodro.alkaa.ui.category.create

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.escodro.alkaa.common.extension.toStringColor
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to [NewCategoryFragment].
 */
class NewCategoryViewModel(private val contract: NewCategoryContract) : ViewModel() {

    val newCategory = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Loads the categoryId based on the given id.
     *
     * @param categoryId categoryId id
     */
    fun loadCategory(categoryId: Long) {
        val disposable = contract.loadCategory(categoryId).subscribe(
            { newCategory.value = it.name },
            { Timber.e("Category not found in database") })

        compositeDisposable.add(disposable)
    }

    /**
     * Add a new category.
     */
    fun addCategory(
        onEmptyField: () -> Unit,
        getCategoryColor: () -> Int?,
        onCategoryAdded: () -> Unit
    ) {
        val name = newCategory.value
        if (TextUtils.isEmpty(name)) {
            onEmptyField()
            return
        }

        val color = getCategoryColor()?.toStringColor()
        val category = Category(name = name, color = color)
        val disposable = contract.addCategory(category)
            .doOnComplete { onCategoryAdded() }
            .subscribe()

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }
}
