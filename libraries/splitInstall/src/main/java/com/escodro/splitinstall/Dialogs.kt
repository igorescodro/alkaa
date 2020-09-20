package com.escodro.splitinstall

import androidx.annotation.LayoutRes
import androidx.annotation.StringRes

/**
 * Data class to represent a Confirmation Dialog in Split Install flow.
 *
 * @param title the dialog title
 * @param message the dialog message
 */
data class ConfirmationDialog(
    @StringRes var title: Int = R.string.split_confirmation_install_title,
    @StringRes var message: Int = R.string.split_confirmation_install_description
)

/**
 * Data class to represent a Loading Dialog in Split Install flow.
 *
 * @param title the dialog title
 * @param message the dialog message
 * @param layout the custom dialog layout
 */
data class LoadingDialog(
    @StringRes var title: Int = R.string.split_downloading_title,
    @StringRes var message: Int = R.string.split_downloading_description,
    @LayoutRes var layout: Int = R.layout.dialog_install_state
)
