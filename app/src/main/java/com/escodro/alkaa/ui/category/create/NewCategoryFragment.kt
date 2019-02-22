package com.escodro.alkaa.ui.category.create

import android.animation.ObjectAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.getTintColor
import com.escodro.alkaa.common.extension.showKeyboard
import com.escodro.alkaa.databinding.FragmentCategoryNewBinding
import kotlinx.android.synthetic.main.fragment_category_new.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to create a new [com.escodro.alkaa.data.local.model.Category].
 */
class NewCategoryFragment : androidx.fragment.app.Fragment() {

    private var binding: FragmentCategoryNewBinding? = null

    private val viewModel: NewCategoryViewModel by viewModel()

    private var categoryColor: Int = R.color.colorAccent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_new, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        binding?.setLifecycleOwner(this)
        binding?.buttonCategorynewAdd?.setOnClickListener {
            viewModel.addCategory(
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

        val categoryId = arguments?.let { NewCategoryFragmentArgs.fromBundle(it).categoryId }

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

        edittext_categorynew_description?.error = getString(R.string.task_error_empty)
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

        return checked?.getTintColor() ?: R.color.colorAccent
    }

    private fun updateScreenColor() {
        val newColor = getCategoryColor()
        ObjectAnimator
            .ofArgb(button_categorynew_add, ANIM_PROPERTY_NAME, categoryColor, newColor).start()
        edittext_categorynew_description?.backgroundTintList = ColorStateList.valueOf(newColor)
        categoryColor = newColor
    }

    companion object {

        private const val ANIM_PROPERTY_NAME = "backgroundColor"
    }
}
