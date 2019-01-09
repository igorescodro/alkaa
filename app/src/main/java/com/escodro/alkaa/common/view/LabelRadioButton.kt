package com.escodro.alkaa.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatRadioButton
import androidx.core.content.ContextCompat
import com.escodro.alkaa.R

/**
 * Custom view to load and change the color of a given shape.
 */
class LabelRadioButton : AppCompatRadioButton {

    private var hexCode: String? = DEFAULT_COLOR

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
        super(context, attrs, defStyleAttr)

    init {
        buttonDrawable = null
        setOnCheckedChangeListener { _, isChecked -> updateView(isChecked) }
    }

    private fun updateView(isChecked: Boolean) {
        if (isChecked) {
            updateViewToChecked()
        } else {
            updateViewToUnchecked()
        }
    }

    private fun updateViewToUnchecked() {
        elevation = 0F
        background = null
        val label = setShapeColor(hexCode, R.drawable.ic_category_label)
        setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
        setCompoundDrawablesWithIntrinsicBounds(label, null, null, null)
        compoundDrawablePadding = dpInPixel(DRAWABLE_PADDING).toInt()
    }

    private fun updateViewToChecked() {
        elevation = dpInPixel(VIEW_ELEVATION)
        background = setShapeColor(hexCode, R.drawable.shape_category_label)
        setTextColor(ContextCompat.getColor(context, android.R.color.white))
        val label = setShapeColor(DEFAULT_COLOR, R.drawable.ic_category_label)
        setCompoundDrawablesWithIntrinsicBounds(label, null, null, null)
    }

    /**
     * Sets the label color.
     *
     * @param hexCode hex color in String format
     */
    fun setLabelColor(hexCode: String?) {
        this.hexCode = hexCode
        updateView(isChecked)
    }

    private fun setShapeColor(hexCode: String?, @DrawableRes resId: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, resId)
        drawable?.mutate()?.colorFilter =
            PorterDuffColorFilter(Color.parseColor(hexCode), PorterDuff.Mode.MULTIPLY)

        return drawable
    }

    private fun dpInPixel(dp: Int): Float =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        )

    companion object {

        private const val DEFAULT_COLOR = "#FFFFFF"

        private const val VIEW_ELEVATION = 4

        private const val DRAWABLE_PADDING = 8
    }
}
