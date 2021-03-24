package com.escodro.theme.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.theme.AlkaaTheme

/**
 * Default dialog with confirm and dismiss button.
 *
 * @param arguments arguments to compose the dialog
 * @param isDialogOpen flag to indicate if the dialog should be open
 * @param onCloseDialog function to be called dismissing the dialog
 */
@Composable
fun AlkaaDialog(
    arguments: DialogArguments,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onCloseDialog,
            title = { Text(text = arguments.title) },
            text = { Text(text = arguments.text) },
            confirmButton = {
                Button(
                    onClick = {
                        arguments.onConfirmAction()
                        onCloseDialog()
                    }
                ) {
                    Text(text = arguments.confirmText)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = onCloseDialog) {
                    Text(text = arguments.dismissText)
                }
            }
        )
    }
}

/**
 * Arguments to be used with [AlkaaDialog].
 *
 * @param title the dialog title
 * @param text the dialog content text
 * @param confirmText the text to be used in the confirm button
 * @param dismissText the text to be used in the dismiss button
 * @param onConfirmAction the action to be executed when the user confirms the dialog
 */
data class DialogArguments(
    val title: String,
    val text: String,
    val confirmText: String,
    val dismissText: String,
    val onConfirmAction: () -> Unit
)

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun DialogPreview() {
    AlkaaTheme {
        val arguments = DialogArguments(
            title = "Something regrettable",
            text = "Are you sure that do you want to do something regrettable?",
            confirmText = "Regret",
            dismissText = "Cancel",
            onConfirmAction = {}
        )

        AlkaaDialog(arguments = arguments, isDialogOpen = true, onCloseDialog = {})
    }
}
