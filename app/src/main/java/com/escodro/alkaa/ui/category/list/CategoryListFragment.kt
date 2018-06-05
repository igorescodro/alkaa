package com.escodro.alkaa.ui.category.list

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.databinding.FragmentCategoryListBinding
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

/**
 * [Fragment] responsible to show all [Category].
 */
class CategoryListFragment : Fragment(), CategoryListDelegate {

    private val adapter: CategoryListAdapter by inject()

    private val viewModel: CategoryListViewModel by viewModel()

    private var binding: FragmentCategoryListBinding? = null

    private var navigator: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_category_list, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindComponents()
        viewModel.delegate = this
        viewModel.loadCategories()
        navigator = NavHostFragment.findNavController(this)
    }

    private fun bindComponents() {
        binding?.setLifecycleOwner(this)
        binding?.recyclerviewCategorylistList?.adapter = adapter
        binding?.recyclerviewCategorylistList?.layoutManager = getLayoutManager()
        binding?.buttonCategorylistAdd
            ?.setOnClickListener { navigator?.navigate(R.id.action_new_category) }
    }

    private fun getLayoutManager() =
        GridLayoutManager(context, NUMBER_OF_COLUMNS)

    override fun updateList(list: MutableList<Category>) {
        adapter.updateCategoryList(list)
    }

    companion object {

        private const val NUMBER_OF_COLUMNS = 2
    }
}
