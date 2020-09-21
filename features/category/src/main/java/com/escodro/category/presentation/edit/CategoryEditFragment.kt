package com.escodro.category.presentation.edit

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.escodro.category.R
import com.escodro.category.model.Category
import com.escodro.category.presentation.CategoryDetailViewController
import com.escodro.core.extension.getChildren
import com.escodro.core.extension.getTintColor
import com.escodro.core.extension.showKeyboard
import com.escodro.core.extension.toStringColor
import kotlinx.android.synthetic.main.fragment_category_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to edit a existing Category.
 */
internal class CategoryEditFragment : Fragment() {

    private val viewModel: CategoryEditViewModel by viewModel()

    private val viewController: CategoryDetailViewController by inject()

    private val navController by lazy { NavHostFragment.findNavController(this) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_category_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        openSoftKeyboard()
        initComponents()
        initListeners()
    }

    private fun initComponents() {
        val id = arguments?.let { CategoryEditFragmentArgs.fromBundle(it).categoryId } ?: 0L

        if (id == 0L) {
            onError()
        } else {
            viewModel.loadCategory(id)
        }
    }

    private fun initListeners() {
        button_categorynew_add.setOnClickListener { saveCategory() }

        viewModel.uiState.observe(
            viewLifecycleOwner,
            { viewState ->
                when (viewState) {
                    CategoryEditUIState.Saved -> onCategorySaved()
                    CategoryEditUIState.EmptyName -> onEmptyField()
                    is CategoryEditUIState.Loaded -> onCategoryLoaded(viewState.category)
                    CategoryEditUIState.Error -> onError()
                }
            }
        )

        radiogroup_categorynew_label.setOnCheckedChangeListener { _, _ -> animateColorTransition() }
    }

    private fun saveCategory() {
        val name = edittext_categorynew_description.text.toString()
        viewModel.saveCategory(name, getCategoryColor().toStringColor())
    }

    private fun onCategorySaved() =
        viewController.onCategorySaved(edittext_categorynew_description, navController)

    private fun onEmptyField() =
        viewController.onEmptyField(requireContext(), edittext_categorynew_description)

    private fun onCategoryLoaded(category: Category) {
        Timber.d("onCategoryLoaded")

        edittext_categorynew_description?.setText(category.name)
        category.color?.let { color -> updateSelectedColor(color) }
    }

    private fun onError() =
        viewController.onError(navController)

    private fun getCategoryColor() =
        viewController.getCategoryColor(radiogroup_categorynew_label)

    private fun animateColorTransition() {
        val currentColor = viewModel.currentColor ?: return

        val newColor = viewController.animateColorTransition(
            Color.parseColor(currentColor),
            radiogroup_categorynew_label,
            edittext_categorynew_description,
            button_categorynew_add
        )

        viewModel.currentColor = newColor.toStringColor()
    }

    private fun updateSelectedColor(color: String) {
        val categories = radiogroup_categorynew_label.getChildren<RadioButton>()
        val intColor = Color.parseColor(color)

        val checked = categories.firstOrNull {
            it.getTintColor() == Color.parseColor(color)
        }

        checked?.isChecked = true
        button_categorynew_add.setBackgroundColor(intColor)
    }

    private fun openSoftKeyboard() {
        edittext_categorynew_description.requestFocus()
        showKeyboard()
    }
}
