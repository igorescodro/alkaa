package com.escodro.task.presentation.detail.category

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.escodro.domain.viewdata.ViewData
import com.escodro.task.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.android.synthetic.main.fragment_task_detail_category.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * [Fragment] responsible to show the [com.escodro.alkaa.data.local.model.Category] options .
 */
class TaskCategoryFragment : Fragment() {

    private val viewModel: TaskCategoryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        inflater.inflate(R.layout.fragment_task_detail_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCategories(onCategoryListLoaded = ::updateCategoryChips)
        chipgrp_taskdetail_category.setOnCheckedChangeListener(::updateTaskWithCategory)
    }

    private fun updateCategoryChips(list: List<ViewData.Category>) {
        Timber.d("updateCategoryChips() - Size = ${list.size}")

        val chipGroup = chipgrp_taskdetail_category

        if (list.isEmpty()) {
            onCategoryListEmpty(chipGroup)
        } else {
            createChipGroup(list, chipGroup)
        }
    }

    private fun createChipGroup(list: List<ViewData.Category>, chipGroup: ChipGroup) {
        list.forEach { category ->
            val chip = createChip(category)
            chipGroup.addView(chip)
            Timber.d("addingChip = ${chip.text}")
        }

        viewModel.taskData.observe(this, Observer { updateCheckedChip(list, chipGroup) })
    }

    private fun updateCheckedChip(list: List<ViewData.Category>, chipGroup: ChipGroup) {
        val checked = list.withIndex().firstOrNull {
            it.value.id == viewModel.taskData.value?.categoryId
        }

        val index = checked?.index ?: return
        val chip = chipGroup.getChildAt(index) as? Chip ?: return
        chip.isChecked = true

        Timber.d("updateCheckedChip = ${chip.text}")
    }

    private fun onCategoryListEmpty(chipGroup: ChipGroup) {
        val emptyChip = Chip(context)
        emptyChip.text = context?.getString(R.string.task_details_category_empty)
        chipGroup.addView(emptyChip)
        return
    }

    private fun createChip(category: ViewData.Category) =
        Chip(context).apply {
            text = category.name
            tag = category.id
            chipBackgroundColor = getChipBackgroundColors(category)
            chipStrokeWidth = CHIP_STROKE_WIDTH
            chipStrokeColor = getChipTextColors(category)
            isCheckedIconVisible = false
            isClickable = true
            isCheckable = true
            setTextColor(context.getColorStateList(R.color.chip_text))
        }

    private fun getChipBackgroundColors(category: ViewData.Category): ColorStateList {
        val colors = intArrayOf(Color.parseColor(category.color), Color.WHITE)
        return ColorStateList(chipStates, colors)
    }

    private fun getChipTextColors(category: ViewData.Category): ColorStateList {
        val colors = context?.let {
            intArrayOf(Color.parseColor(category.color), it.getColor(R.color.gray_light))
        }

        return ColorStateList(chipStates, colors)
    }

    private fun updateTaskWithCategory(chipGroup: ChipGroup?, position: Int) {
        Timber.d("updateTaskWithCategory() - Position = $position")

        val checked = chipGroup?.findViewById<Chip>(position)
        val categoryId = checked?.tag as? Long
        viewModel.updateCategory(categoryId)
    }

    companion object {

        private const val CHIP_STROKE_WIDTH = 2F

        private val chipStates = arrayOf(
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked)
        )
    }
}
