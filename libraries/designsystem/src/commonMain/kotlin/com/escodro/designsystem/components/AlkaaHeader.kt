package com.escodro.designsystem.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Alkaa Header component used in the Task Detail screen.
 *
 * @param text the text to be shown in the Header
 * @param onTextChange the callback to be called when the text changes
 * @param interactionSource the interaction source to be applied to the Header
 * @param isChecked the state of the checkbox
 * @param onCheckedChange the callback to be called when the checkbox state changes
 * @param isSinglePane whether the app is in single pane mode
 * @param onUpPress the callback to be called when the up button is pressed
 * @param modifier the modifier to be applied to the Header
 */
@Composable
fun AlkaaHeader(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isSinglePane: Boolean,
    onUpPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val headerShape = MaterialTheme.shapes.medium.copy(
        topEnd = CornerSize(0.dp),
        topStart = CornerSize(0.dp)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(headerShape)
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryFixedDim,
                shape = headerShape
            )
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        val iconType = if (isSinglePane) HeaderIconType.Back else HeaderIconType.Close
        IconButton(
            onClick = onUpPress,
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = iconType.imageVector,
                contentDescription = iconType.contentDescription,
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
                    .padding(bottom = 14.dp)
                    .align(Alignment.Bottom)
                    .weight(1f)
            )
            OutlinedTextField(
                value = text,
                onValueChange = onTextChange,
                textStyle = MaterialTheme.typography.headlineLarge
                    .copy(
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (isChecked) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    ),
                interactionSource = interactionSource,
                colors = OutlinedTextFieldDefaults.colors()
                    .copy(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                modifier = Modifier.weight(5f)

            )
        }
    }
}

private enum class HeaderIconType(
    val imageVector: ImageVector,
    val contentDescription: String?,
) {
    Back(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"),
    Close(imageVector = Icons.Rounded.Close, contentDescription = "Close"),
}

@Preview(showBackground = true)
@Composable
private fun AlkaaHeaderPreview() {
    var textFieldValue: TextFieldValue by remember { mutableStateOf(TextFieldValue("Custom text")) }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    var isChecked by remember { mutableStateOf(false) }
    AlkaaThemePreview {
        Scaffold(
            topBar = {
                AlkaaHeader(
                    text = textFieldValue,
                    onTextChange = { newText -> textFieldValue = newText },
                    interactionSource = interactionSource,
                    isChecked = isChecked,
                    onCheckedChange = { newValue -> isChecked = newValue },
                    isSinglePane = true,
                    onUpPress = {},
                    modifier = Modifier.fillMaxHeight(0.25f)
                )
            }) {}
    }
}
