package com.escodro.task.presentation.list.model

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.CallbackProp
import com.airbnb.epoxy.ModelView
import com.escodro.core.extension.onActionDone
import com.escodro.task.R

/**
 * [ModelView] representing an item to add a new task.
 */
@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
internal class AddEntryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val descriptionEditText: EditText

    private val addImageView: ImageView

    init {
        View.inflate(context, R.layout.item_add_task, this)
        descriptionEditText = findViewById(R.id.edittext_itemadd_description)
        addImageView = findViewById(R.id.imageview_itemadd_completed)
    }

    @CallbackProp
    fun setOnActionDone(onActionDone: ((String) -> Unit)?) {
        descriptionEditText.onActionDone { text -> onActionDone?.invoke(text) }
    }

    @CallbackProp
    fun setOnAddClicked(onAddClicked: (() -> Unit)?) {
        addImageView.setOnClickListener { onAddClicked?.invoke() }
    }
}
