package com.escodro.search.presentation

import androidx.lifecycle.ViewModel
import com.escodro.domain.model.TaskWithCategory
import com.escodro.domain.usecase.search.SearchTasksByName
import com.escodro.search.mapper.TaskSearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

internal class SearchViewModel(
    private val findTaskUseCase: SearchTasksByName,
    private val mapper: TaskSearchMapper
) : ViewModel() {

    fun findTasksByName(name: String = ""): Flow<SearchViewState> = flow {
        findTaskUseCase(name).collect { taskList ->
            val state = if (taskList.isNotEmpty()) {
                onListLoaded(taskList)
            } else {
                SearchViewState.Empty
            }
            emit(state)
        }
    }

    private fun onListLoaded(taskList: List<TaskWithCategory>): SearchViewState {
        val searchList = mapper.toTaskSearch(taskList)
        return SearchViewState.Loaded(searchList)
    }
}
