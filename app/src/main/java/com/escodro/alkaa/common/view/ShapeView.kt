package com.escodro.alkaa.common.view

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.escodro.alkaa.R

/**
 * Custom view to load and change the color od a given shape.
 */
class ShapeView : AppCompatImageView {

    private var hexColor: String = DEFAULT_COLOR

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        updateAttrs(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        updateAttrs(attrs)
    }

    private fun updateAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.ShapeView)
            hexColor = typedArray.getString(R.styleable.ShapeView_shapeColor)
            typedArray.recycle()
        }

        setDrawableColor(hexColor)
    }

    private fun setDrawableColor(hexCode: String) {
        val drawable = drawable.mutate()
        drawable.colorFilter =
                PorterDuffColorFilter(Color.parseColor(hexCode), PorterDuff.Mode.MULTIPLY)
    }

    companion object {

        private const val DEFAULT_COLOR = "#FFFFFF"
    }
}
