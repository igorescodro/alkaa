package com.escodro.designsystem.components.kuvio.bar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.icon.button.KuvioAddButton
import com.escodro.designsystem.components.kuvio.icon.button.KuvioCalendarIcon
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.kuvio_add_task_bar_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * A persistent input bar anchored at the bottom of every task list screen.
 *
 * In the **resting** state the bar shows only the text field with a placeholder string.
 * When the embedded text field gains focus (the **focused/typing** state) the border switches
 * to [MaterialTheme.colorScheme.primary] and two action icons animate in on the trailing edge:
 * a calendar icon ([KuvioCalendarIcon]) and an add button ([KuvioAddButton]).
 *
 * @param value current text entered by the user.
 * @param onValueChange callback invoked on every keystroke.
 * @param onAddClick callback invoked when the trailing circular "+" button is tapped.
 *   Only visible when the bar is in the focused/typing state.
 * @param onDateClick callback invoked when the trailing calendar icon is tapped.
 *   Only visible when the bar is in the focused/typing state.
 * @param modifier modifier applied to the outermost [Surface].
 */
@Composable
fun KuvioAddTaskBar(
    value: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDateClick: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    val taskBarShape = RoundedCornerShape(14.dp)
    var isFocused by remember { mutableStateOf(false) }
    val borderColor = if (isFocused) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.outlineVariant
    }
    val elevation = if (isFocused) 4.dp else 2.dp

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = taskBarShape),
        shape = taskBarShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = elevation,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Spacer(modifier = Modifier.width(12.dp))
            AddTaskTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = stringResource(Res.string.kuvio_add_task_bar_placeholder),
                onFocus = { isFocused = it },
                modifier = Modifier.weight(1f),
            )
            AddTaskActions(
                visible = isFocused,
                onDateClick = onDateClick,
                onAddClick = onAddClick,
            )
        }
    }
}

@Composable
private fun AddTaskTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onFocus: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.onFocusChanged { onFocus(it.isFocused) },
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface,
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box {
                if (value.isEmpty()) {
                    KuvioBodyMediumText(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                innerTextField()
            }
        },
    )
}

@Composable
private fun AddTaskActions(
    visible: Boolean,
    onDateClick: (() -> Unit),
    onAddClick: (() -> Unit),
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + expandHorizontally(),
        exit = fadeOut() + shrinkHorizontally(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KuvioCalendarIcon(
                onClick = onDateClick,
                modifier = Modifier.size(24.dp),
            )
            KuvioAddButton(
                onClick = onAddClick,
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioAddTaskBarLightPreview() {
    var text by remember { mutableStateOf("") }
    AlkaaThemePreview {
        KuvioAddTaskBar(
            value = text,
            onValueChange = { text = it },
            onAddClick = {},
            onDateClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioAddTaskBarDarkPreview() {
    var text by remember { mutableStateOf("") }
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioAddTaskBar(
            value = text,
            onValueChange = { text = it },
            onAddClick = {},
            onDateClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
