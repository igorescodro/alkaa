package com.escodro.alkaa.ui.category.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.databinding.FragmentCategoryListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show all [Category].
 */
class CategoryListFragment : Fragment(), CategoryListAdapter.CategoryListListener {

    private val adapter = CategoryListAdapter()

    private val viewModel: CategoryListViewModel by viewModel()

    private var binding: FragmentCategoryListBinding? = null

    private var navigator: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_category_list, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        bindComponents()
        adapter.listener = this
        viewModel.loadCategories(onListLoaded = ::updateList)
        navigator = NavHostFragment.findNavController(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("onDestroyView()")

        adapter.listener = null
    }

    private fun bindComponents() {
        Timber.d("bindComponents()")

        binding?.setLifecycleOwner(this)
        binding?.recyclerviewCategorylistList?.adapter = adapter
        binding?.recyclerviewCategorylistList?.layoutManager = getLayoutManager()
        binding?.buttonCategorylistAdd
            ?.setOnClickListener { navigator?.navigate(R.id.action_new_category) }
    }

    private fun getLayoutManager() =
        GridLayoutManager(context, NUMBER_OF_COLUMNS)

    private fun updateList(list: List<Category>) {
        Timber.d("updateList() - Size = ${list.size}")

        adapter.updateList(list)
    }

    override fun onOptionMenuClicked(view: View, category: Category) {
        Timber.d("onOptionMenuClicked() - clicked = ${category.name}")

        val popupMenu = context?.let { PopupMenu(it, view) }
        val inflater = popupMenu?.menuInflater
        inflater?.inflate(R.menu.category_menu, popupMenu.menu)
        popupMenu?.setOnMenuItemClickListener(onMenuItemClicked(category))
        popupMenu?.show()
    }

    private fun onTaskRemoved(category: Category) {
        Timber.d("onTaskRemoved() - clicked = ${category.name}")

        adapter.removeItem(category)
    }

    private fun onMenuItemClicked(category: Category) =
        PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.key_action_remove_category -> viewModel.deleteCategory(
                    category,
                    onCategoryRemoved = ::onTaskRemoved
                )
            }
            true
        }

    companion object {

        private const val NUMBER_OF_COLUMNS = 2
    }
}
