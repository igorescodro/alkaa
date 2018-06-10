package com.escodro.alkaa.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatRadioButton
import android.util.AttributeSet
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
            background = setShapeColor(hexCode, R.drawable.shape_category_label)
            setTextColor(ContextCompat.getColor(context, android.R.color.primary_text_dark))
            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            background = null
            val label = setShapeColor(hexCode, R.drawable.ic_category_label)
            setTextColor(ContextCompat.getColor(context, android.R.color.primary_text_light))
            setCompoundDrawablesWithIntrinsicBounds(label, null, null, null)
        }
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

    companion object {

        private const val DEFAULT_COLOR = "#FFFFFF"
    }
}
