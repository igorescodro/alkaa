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

    private val _state: MutableState<SearchUIState> = mutableStateOf(SearchUIState.Empty)

    val state: State<SearchUIState>
        get() = _state

    fun findTasksByName(name: String = "") {
        viewModelScope.launch {
            val taskList = findTaskUseCase(name)
            _state.value = if (taskList.isNotEmpty()) {
                onListLoaded(taskList)
            } else {
                SearchUIState.Empty
            }
        }
    }

    private fun onListLoaded(taskList: List<TaskWithCategory>): SearchUIState {
        val searchList = mapper.toTaskSearch(taskList)
        return SearchUIState.Loaded(searchList)
    }
}
