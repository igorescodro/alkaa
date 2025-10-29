package com.escodro.designsystem.components.textfield

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.escodro.designsystem.AlkaaThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * TextField input for Alkaa forms.
 *
 * @param label text field label
 * @param text text to be shown
 * @param onTextChange function to update text
 * @param modifier text field modifier
 */
@Composable
fun AlkaaInputTextField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        label = { Text(text = label) },
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
private fun AlkaaInputTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    AlkaaThemePreview {
        AlkaaInputTextField(
            label = "Your text here",
            text = text,
            onTextChange = { text = it },
        )
    }
}
