package com.escodro.alkaa.ui.category.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.dialog
import com.escodro.alkaa.common.extension.negativeButton
import com.escodro.alkaa.common.extension.positiveButton
import com.escodro.alkaa.data.local.model.Category
import kotlinx.android.synthetic.main.fragment_category_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show all [Category].
 */
class CategoryListFragment : Fragment(), CategoryListAdapter.CategoryListListener {

    private val adapter = CategoryListAdapter()

    private val viewModel: CategoryListViewModel by viewModel()

    private var navigator: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        return inflater.inflate(R.layout.fragment_category_list, container, false)
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

        recyclerview_categorylist_list?.adapter = adapter
        recyclerview_categorylist_list?.layoutManager = getLayoutManager()
        button_categorylist_add?.setOnClickListener { navigator?.navigate(R.id.action_new_category) }
    }

    private fun getLayoutManager() =
        GridLayoutManager(context, NUMBER_OF_COLUMNS)

    private fun updateList(list: List<Category>) {
        Timber.d("updateList() - Size = ${list.size}")

        adapter.submitList(list)
    }

    override fun onOptionMenuClicked(view: View, category: Category) {
        Timber.d("onOptionMenuClicked() - clicked = ${category.name}")

        val popupMenu = context?.let { PopupMenu(it, view) }
        val inflater = popupMenu?.menuInflater
        inflater?.inflate(R.menu.category_menu, popupMenu.menu)
        popupMenu?.setOnMenuItemClickListener(onMenuItemClicked(category))
        popupMenu?.show()
    }

    private fun onMenuItemClicked(category: Category) =
        PopupMenu.OnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.key_action_remove_category -> confirmRemoval(category)
            }
            true
        }

    private fun confirmRemoval(category: Category) {
        val description = getString(R.string.category_list_dialog_remove_description, category.name)

        dialog(R.string.category_list_dialog_remove_title, description) {
            positiveButton(R.string.category_list_dialog_remove_positive) { removeCategory(category) }
            negativeButton(R.string.category_list_dialog_remove_negative) { /* Do nothing */ }
        }.show()
    }

    private fun removeCategory(category: Category) {
        viewModel.deleteCategory(category)
    }

    companion object {

        private const val NUMBER_OF_COLUMNS = 2
    }
}
