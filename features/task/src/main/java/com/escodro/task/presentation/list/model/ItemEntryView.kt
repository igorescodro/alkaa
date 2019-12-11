package com.escodro.task.presentation.list.model

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.escodro.core.extension.formatRelativeDate
import com.escodro.core.extension.setStyleDisable
import com.escodro.task.R
import java.util.Calendar

/**
 * [ModelView] representing an item with task information.
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
internal class ItemEntryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val backgroundCardView: CardView

    private val completedCheckbox: CheckBox

    private val descriptionTextView: TextView

    private val alarmTextView: TextView

    private val colorView: View

    init {
        View.inflate(context, R.layout.item_task, this)
        backgroundCardView = findViewById(R.id.cardview_itemtask_background)
        completedCheckbox = findViewById(R.id.checkbox_itemtask_completed)
        descriptionTextView = findViewById(R.id.textview_itemtask_description)
        alarmTextView = findViewById(R.id.textview_itemtask_alarm)
        colorView = findViewById(R.id.view_itemtask_color)
    }

    @ModelProp
    fun setCheckedState(isChecked: Boolean) {
        completedCheckbox.isChecked = isChecked

        if (isChecked) {
            descriptionTextView.setStyleDisable()
            alarmTextView.setStyleDisable()
        }
    }

    @TextProp
    fun setDescription(description: CharSequence) {
        descriptionTextView.text = description
    }

    @ModelProp
    fun setAlarm(alarm: Calendar?) {
        if (alarm == null) {
            return
        }

        alarmTextView.apply {
            visibility = View.VISIBLE
            text = context?.formatRelativeDate(alarm.timeInMillis)
        }
    }

    @TextProp
    fun setColor(color: CharSequence?) {
        if (color.isNullOrEmpty()) {
            return
        }

        colorView.setBackgroundColor(Color.parseColor(color.toString()))
    }

    @CallbackProp
    fun setOnItemClicked(onItemClicked: (() -> Unit)?) {
        backgroundCardView.setOnClickListener { onItemClicked?.invoke() }
    }

    @CallbackProp
    fun setOnItemLongPressed(onItemLongPressed: (() -> Unit)?) {
        backgroundCardView.setOnLongClickListener {
            onItemLongPressed?.invoke()
            true
        }
    }

    @CallbackProp
    fun setOnItemCheckedChanged(onItemCheckedChanged: ((Boolean) -> Unit)?) {
        completedCheckbox.setOnClickListener { view ->
            val isChecked = (view as? CheckBox)?.isChecked ?: false
            onItemCheckedChanged?.invoke(isChecked)
        }
    }
}
