package com.escodro.alkaa.ui.category.create

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.text.TextUtils
import com.escodro.alkaa.data.local.model.Category
import io.reactivex.disposables.CompositeDisposable

/**
 * [ViewModel] responsible to provide information to [NewCategoryFragment].
 *
 * Created by Igor Escodro on 3/6/18.
 */
class NewCategoryViewModel(private val contract: NewCategoryContract) : ViewModel() {

    var delegate: NewCategoryDelegate? = null

    val newCategory = MutableLiveData<String>()

    private val compositeDisposable = CompositeDisposable()

    /**
     * Add a new category.
     */
    fun addCategory() {
        val name = newCategory.value
        if (TextUtils.isEmpty(name)) {
            delegate?.onEmptyField()
            return
        }

        val color = delegate?.getCategoryColor()
        val category = Category(name = name, color = color)
        val disposable = contract.addCategory(category)
            .doOnComplete { delegate?.onNewCategoryAdded() }
            .subscribe()

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
        delegate = null
    }
}
