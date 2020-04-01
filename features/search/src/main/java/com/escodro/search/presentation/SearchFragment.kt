package com.escodro.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.escodro.core.extension.textChangedFlow
import com.escodro.search.R
import com.escodro.search.model.TaskSearch
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Fragment to search for Tasks.
 */
internal class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    private val adapter = GroupAdapter<GroupieViewHolder>()

    private val navigator: NavController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated")

        layout_search.transitionToEnd()
        recyclerview_search.adapter = adapter
        initListeners()
        viewModel.findTasksByName()
    }

    private fun initListeners() {
        viewModel.state.observe(this, Observer { state ->
            when (state) {
                is SearchUIState.Loaded -> onListLoaded(state.taskList)
                is SearchUIState.Empty -> onEmptyList()
            }
        })

        lifecycleScope.launch {
            textview_search_query.textChangedFlow()
                .collect { query -> viewModel.findTasksByName(query) }
        }
    }

    private fun onListLoaded(taskList: List<TaskSearch>) {
        Timber.d("onListLoaded - Size = ${taskList.size}")

        recyclerview_search.visibility = View.VISIBLE
        val items = taskList.map { TaskSearchItem(it, ::onItemClicked) }
        adapter.update(items)
    }

    private fun onItemClicked(itemId: Long) {
        Timber.d("onListLoaded - itemId = $itemId")
        val directions = SearchFragmentDirections.actionSearchDetail(itemId)
        navigator.navigate(directions)
    }

    private fun onEmptyList() {
        Timber.d("onEmptyList")

        recyclerview_search.visibility = View.INVISIBLE
        adapter.update(listOf())
    }

    override fun onDestroyView() {
        super.onDestroyView()

        layout_search.transitionToStart()
    }
}
