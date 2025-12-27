package com.escodro.designsystem.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Default dialog with confirm and dismiss button.
 *
 * @param arguments arguments to compose the dialog
 * @param isDialogOpen flag to indicate if the dialog should be open
 * @param onDismissRequest function to be called user requests to dismiss the dialog
 * @param modifier the modifier to be applied to the dialog
 */
@Composable
fun AlkaaDialog(
    arguments: DialogArguments,
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = arguments.title) },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(text = arguments.text)
                }
            },
            confirmButton = {
                Button(onClick = arguments.onConfirmAction) {
                    Text(text = arguments.confirmText)
                }
            },
            dismissButton = {
                arguments.dismissText?.let {
                    OutlinedButton(onClick = onDismissRequest) {
                        Text(text = arguments.dismissText)
                    }
                }
            },
            modifier = modifier,
        )
    }
}

/**
 * Arguments to be used with [AlkaaDialog].
 *
 * @property title the dialog title
 * @property text the dialog content text
 * @property confirmText the text to be used in the confirm button
 * @property dismissText the text to be used in the dismiss button
 * @property onConfirmAction the action to be executed when the user confirms the dialog
 */
data class DialogArguments(
    val title: String,
    val text: String,
    val confirmText: String,
    val dismissText: String? = null,
    val onConfirmAction: () -> Unit,
)

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
private fun DialogPreview() {
    AlkaaThemePreview {
        val arguments = DialogArguments(
            title = "Something regrettable",
            text = "Are you sure that do you want to do something regrettable?",
            confirmText = "Regret",
            dismissText = "Cancel",
            onConfirmAction = {},
        )

        AlkaaDialog(arguments = arguments, isDialogOpen = true, onDismissRequest = {})
    }
}
