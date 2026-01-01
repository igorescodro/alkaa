package com.escodro.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

@Composable
fun AlkaaHeader(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    interactionSource: MutableInteractionSource,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val headerShape = MaterialTheme.shapes.medium.copy(
        topEnd = CornerSize(0.dp),
        topStart = CornerSize(0.dp)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(0.25f)
            .clip(headerShape)
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondaryFixedDim,
                shape = headerShape
            )
    ) {
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
                )
            }) {}
    }
}
