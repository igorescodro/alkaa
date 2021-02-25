package com.escodro.search.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.search.mapper.TaskSearchMapper
import kotlinx.coroutines.launch

internal class SearchViewModel(
    private val findTaskUseCase: SearchTasksByName,
    private val mapper: TaskSearchMapper
) : ViewModel() {

    private val _state: MutableState<SearchViewState> = mutableStateOf(SearchViewState.Empty)

    val state: State<SearchViewState>
        get() = _state

    init {
        findTasksByName()
    }

    fun findTasksByName(name: String = "") {
        viewModelScope.launch {
            val taskList = findTaskUseCase(name)
            _state.value = if (taskList.isNotEmpty()) {
                onListLoaded(taskList)
            } else {
                SearchViewState.Empty
            }
        }
    }

    private fun onListLoaded(taskList: List<TaskWithCategory>): SearchViewState {
        val searchList = mapper.toTaskSearch(taskList)
        return SearchViewState.Loaded(searchList)
    }
}
