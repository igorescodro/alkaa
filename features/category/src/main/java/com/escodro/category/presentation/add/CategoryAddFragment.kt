package com.escodro.category.presentation.add

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import com.escodro.category.R
import com.escodro.category.presentation.CategoryDetailViewController
import com.escodro.core.extension.showKeyboard
import com.escodro.core.extension.toStringColor
import kotlinx.android.synthetic.main.fragment_category_detail.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to add a new Category.
 */
internal class CategoryAddFragment : Fragment() {

    private val viewModel: CategoryAddViewModel by viewModel()

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
    }

    private fun initComponents() {
        button_categorynew_add.setOnClickListener {
            val name = edittext_categorynew_description.text.toString()
            viewModel.saveCategory(name = name, color = getCategoryColor().toStringColor())
        }

        viewModel.uiState.observe(viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                CategoryAddUIState.Saved -> onCategorySaved()
                CategoryAddUIState.EmptyName -> onEmptyField()
            }
        })

        viewModel.currentColor = getCategoryColor().toStringColor()
        radiogroup_categorynew_label.setOnCheckedChangeListener { _, _ -> animateColorTransition() }
    }

    private fun onCategorySaved() =
        viewController.onCategorySaved(edittext_categorynew_description, navController)

    private fun onEmptyField() =
        viewController.onEmptyField(requireContext(), edittext_categorynew_description)

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

    private fun openSoftKeyboard() {
        edittext_categorynew_description.requestFocus()
        showKeyboard()
    }
}
