package com.escodro.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.search.mapper.TaskSearchMapper
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * [ViewModel] responsible to provide information to Task Search layout.
 */
internal class SearchViewModel(
    private val findTaskUseCase: SearchTasksByName,
    private val mapper: TaskSearchMapper
) : ViewModel() {

    private val _state = MutableLiveData<SearchUIState>()

    val state: LiveData<SearchUIState>
        get() = _state

    /**
     * Finds the task by name.
     *
     * @param name the task name to be queried
     */
    fun findTasksByName(name: String = "") {
        viewModelScope.launch {
            val taskList = findTaskUseCase(name)
            _state.value = if (taskList.isNotEmpty()) {
                onListLoaded(taskList)
            } else {
                SearchUIState.Empty
            }

            Timber.d("findTasksByName = $name | state = ${state.value}")
        }
    }

    private fun onListLoaded(taskList: List<TaskWithCategory>): SearchUIState {
        val searchList = mapper.toTaskSearch(taskList)
        return SearchUIState.Loaded(searchList)
    }
}
