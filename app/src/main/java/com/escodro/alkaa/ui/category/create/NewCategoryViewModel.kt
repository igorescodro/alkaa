package com.escodro.alkaa.ui.category.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.text.TextUtils
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [NewCategoryFragment].
 *
 * Created by Igor Escodro on 3/6/18.
 */
class NewCategoryViewModel(private val contract: NewCategoryContract) : ViewModel() {

    val newCategory = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Add a new category.
     */
    fun addCategory(
        onEmptyField: () -> Unit,
        getCategoryColor: () -> String?,
        onCategoryAdded: () -> Unit
    ) {
        val name = newCategory.value
        if (TextUtils.isEmpty(name)) {
            onEmptyField()
            return
        }

        val color = getCategoryColor()
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
