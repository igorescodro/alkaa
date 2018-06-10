package com.escodro.alkaa.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.HorizontalScrollView
import android.widget.RadioGroup
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category

/**
 * Custom view to show all the categories in [LabelRadioButton] format inside a
 * [HorizontalScrollView].
 */
class ScrollableRadioGroup : HorizontalScrollView {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as? LayoutInflater
        inflater?.inflate(R.layout.view_scrollable_radio_group, this)
    }

    /**
     * Adds all the [Category] in the [ScrollableRadioGroup].
     *
     * @param list of all categories
     */
    fun addAll(list: MutableList<Category>) {
        val rb = arrayOfNulls<LabelRadioButton>(list.size)
        val radioGroup = findViewById<RadioGroup>(R.id.srg_radiogroup_list)

        list.forEachIndexed { index, category ->
            rb[index] = LabelRadioButton(context)
            val radioButton = rb[index]

            radioButton?.apply {
                text = category.name
                setLabelColor(category.color)
                tag = category.color

                val params = LayoutParams(LayoutParams.WRAP_CONTENT, dpInPixel(RADIO_HEIGHT))
                setMargin(params, list, index)

                val padding = dpInPixel(RADIO_PADDING)
                setPadding(padding, 0, padding, 0)

                radioGroup.addView(radioButton, params)
            }
        }
    }

    /**
     * Updates the margin based on the item position in the list. If the view is the first one of
     * the list, it adds a margin on the start of the view; if is the last item of the list, it
     * adds a margin on the start of the view.
     *
     * @param params the layout params
     * @param list of all categories
     * @param position item position
     */
    private fun setMargin(params: LayoutParams, list: MutableList<Category>, position: Int) {
        val marginX = dpInPixel(RADIO_MARGIN_X)
        val marginY = dpInPixel(RADIO_MARGIN_Y)

        if (position == 0) {
            params.setMargins(marginX, marginY, 0, marginY)
        } else if (position == list.size - 1) {
            params.setMargins(0, marginY, marginX, marginY)
        }
    }

    private fun dpInPixel(dp: Int): Int =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()

    companion object {

        private const val RADIO_HEIGHT = 32

        private const val RADIO_PADDING = 24

        private const val RADIO_MARGIN_X = 16

        private const val RADIO_MARGIN_Y = 8
    }
}
