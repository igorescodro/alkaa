package com.escodro.alkaa.ui.category.create

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.escodro.alkaa.R
import com.escodro.alkaa.databinding.FragmentCategoryNewBinding
import org.koin.android.architecture.ext.viewModel

/**
 * [Fragment] responsible to create a new [com.escodro.alkaa.data.local.model.Category].
 */
class NewCategoryFragment : Fragment(), NewCategoryDelegate {

    private var binding: FragmentCategoryNewBinding? = null

    private val viewModel: NewCategoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(
                inflater, R.layout.fragment_category_new,
                container, false
            )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindComponents()
        viewModel.delegate = this
    }

    private fun bindComponents() {
        binding?.setLifecycleOwner(this)
        binding?.buttonCategorynewAdd?.setOnClickListener { viewModel.addCategory() }
        binding?.viewModel = viewModel
    }

    override fun onEmptyField() {
        binding?.edittextCategorynewDescription?.error = getString(R.string.task_error_empty)
    }

    override fun onNewCategoryAdded() {
        binding?.edittextCategorynewDescription?.text = null
        NavHostFragment.findNavController(this).navigateUp()
    }
}
