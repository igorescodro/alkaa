package com.escodro.category.presentation.detail

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.escodro.category.R
import com.escodro.category.databinding.FragmentCategoryDetailBinding
import com.escodro.core.extension.getChildren
import com.escodro.core.extension.getTintColor
import com.escodro.core.extension.showKeyboard
import kotlinx.android.synthetic.main.fragment_category_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to create a new [com.escodro.domain.viewdata.ViewData.Category].
 */
class CategoryDetailFragment : Fragment() {

    private var binding: FragmentCategoryDetailBinding? = null

    private val viewModel: CategoryDetailViewModel by viewModel()

    private var categoryColor: Int = android.R.color.white

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_detail, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        binding?.lifecycleOwner = this
        binding?.buttonCategorynewAdd?.setOnClickListener {
            viewModel.saveCategory(
                onEmptyField = ::onEmptyField,
                onCategoryAdded = ::onNewCategoryAdded,
                getCategoryColor = ::getCategoryColor
            )
        }
        binding?.viewModel = viewModel
        initComponents()
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        val categoryId = arguments?.let { CategoryDetailFragmentArgs.fromBundle(it).categoryId }
        categoryId?.let { viewModel.loadCategory(it, ::updateSelectedColor) }

        setupTextInput()
        categoryColor = getCategoryColor()
        updateScreenColor()
        radiogroup_categorynew_label.setOnCheckedChangeListener { _, _ -> updateScreenColor() }
    }

    private fun setupTextInput() {
        edittext_categorynew_description.requestFocus()
        showKeyboard()
    }

    private fun onEmptyField() {
        Timber.d("onEmptyField()")

        edittext_categorynew_description?.error = getString(R.string.category_detail_error_empty)
    }

    private fun onNewCategoryAdded() {
        Timber.d("onNewCategoryAdded()")

        binding?.edittextCategorynewDescription?.text = null
        NavHostFragment.findNavController(this).navigateUp()
    }

    private fun getCategoryColor(): Int {
        Timber.d("getCategoryColor()")

        val radioGroup = binding?.radiogroupCategorynewLabel
        val checkedId = radioGroup?.checkedRadioButtonId
        val checked = checkedId?.let { radioGroup.findViewById<RadioButton>(it) }

        return checked?.getTintColor() ?: android.R.color.white
    }

    private fun updateScreenColor() {
        val newColor = getCategoryColor()
        ObjectAnimator
            .ofArgb(button_categorynew_add, ANIM_PROPERTY_NAME, categoryColor, newColor).start()
        edittext_categorynew_description?.backgroundTintList = ColorStateList.valueOf(newColor)
        categoryColor = newColor
    }

    private fun updateSelectedColor(color: String) {
        val categories = binding?.radiogroupCategorynewLabel?.getChildren<RadioButton>() ?: return

        val checked = categories.firstOrNull {
            it.getTintColor() == Color.parseColor(color)
        }

        checked?.isChecked = true
    }

    companion object {

        private const val ANIM_PROPERTY_NAME = "backgroundColor"
    }
}
