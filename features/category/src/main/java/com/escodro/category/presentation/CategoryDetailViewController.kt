package com.escodro.category.presentation

import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.navigation.NavController
import com.escodro.category.R
import com.escodro.core.extension.getTintColor
import timber.log.Timber

/**
 * The View Controller to be used in different Category detail screens.
 */
internal class CategoryDetailViewController {

    /**
     * Handles the state where the category is saved.
     *
     * @param editText EditText to be updated
     * @param navController controller to navigate between screens
     */
    fun onCategorySaved(editText: EditText, navController: NavController) {
        Timber.d("onCategorySaved")

        editText.text = null
        navController.navigateUp()
    }

    /**
     * Handles the state where the category has a empty name.
     *
     * @param context the application context
     * @param editText the EditText to be updated
     */
    fun onEmptyField(context: Context, editText: EditText) {
        Timber.d("onEmptyField")

        editText.error = context.getString(R.string.category_detail_error_empty)
    }

    /**
     * Handles the state where an error occurred.
     *
     * @param navController controller to navigate between screens
     */
    fun onError(navController: NavController) {
        Timber.d("onError")
        navController.navigateUp()
    }

    /**
     * Gets the category color from the RadioGroup.
     *
     * @param radioGroup the radio group to extract the selected item color from
     *
     * @return the category color
     */
    fun getCategoryColor(radioGroup: RadioGroup): Int {
        val checkedId = radioGroup.checkedRadioButtonId
        val checked = checkedId.let { radioGroup.findViewById<RadioButton>(it) }

        return checked.getTintColor()
    }

    /**
     * Animates the elements color changes.
     *
     * @param oldColor the color to start the animation
     * @param radioGroup the radio group to extract the new color from
     * @param editText the EditText to be updated
     * @param button the Button to be updated
     */
    fun animateColorTransition(
        oldColor: Int,
        radioGroup: RadioGroup,
        editText: EditText,
        button: Button
    ): Int {
        val newColor = getCategoryColor(radioGroup)
        ObjectAnimator.ofArgb(button, ANIM_PROPERTY_NAME, oldColor, newColor).start()
        editText.backgroundTintList = ColorStateList.valueOf(newColor)
        return newColor
    }

    companion object {

        private const val ANIM_PROPERTY_NAME = "backgroundColor"
    }
}
