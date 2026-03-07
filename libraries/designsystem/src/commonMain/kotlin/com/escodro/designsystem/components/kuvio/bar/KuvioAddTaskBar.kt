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
import com.escodro.designsystem.components.kuvio.button.KuvioAddButton
import com.escodro.designsystem.components.kuvio.icon.button.KuvioCalendarIcon
import com.escodro.designsystem.components.kuvio.icon.button.KuvioListIcon
import com.escodro.designsystem.components.kuvio.icon.button.KuvioNotificationsIcon
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.kuvio_add_task_bar_placeholder
import org.jetbrains.compose.resources.stringResource

/**
 * A persistent input bar anchored at the bottom of every task list screen.
 *
 * In the **resting** state the bar shows a circular "add" button and a placeholder string.
 * When the embedded text field gains focus (the **focused/typing** state) the border switches
 * to [MaterialTheme.colorScheme.primary] and three action icons animate in on the trailing edge.
 *
 * @param value current text entered by the user.
 * @param onValueChange callback invoked on every keystroke.
 * @param onAddClick callback invoked when the leading circular "+" button is tapped.
 * @param modifier modifier applied to the outermost [Surface].
 * @param onDateClick optional callback for the date-picker action icon.
 *   When `null` the date icon is not rendered.
 * @param onReminderClick optional callback for the reminder action icon.
 *   When `null` the reminder icon is not rendered.
 * @param onListPickerClick optional callback for the list-picker action icon.
 *   When `null` the list icon is not rendered.
 */
@Composable
fun KuvioAddTaskBar(
    value: String,
    onValueChange: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    onDateClick: (() -> Unit)? = null,
    onReminderClick: (() -> Unit)? = null,
    onListPickerClick: (() -> Unit)? = null,
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
            KuvioAddButton(
                onClick = onAddClick,
                modifier = Modifier.size(32.dp),
            )
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
                onReminderClick = onReminderClick,
                onListPickerClick = onListPickerClick,
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
    onDateClick: (() -> Unit)?,
    onReminderClick: (() -> Unit)?,
    onListPickerClick: (() -> Unit)?,
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
            if (onDateClick != null) {
                KuvioCalendarIcon(
                    onClick = onDateClick,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (onReminderClick != null) {
                KuvioNotificationsIcon(
                    onClick = onReminderClick,
                    modifier = Modifier.size(24.dp),
                )
            }
            if (onListPickerClick != null) {
                KuvioListIcon(
                    onClick = onListPickerClick,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioAddTaskBarRestingLightPreview() {
    AlkaaThemePreview {
        KuvioAddTaskBar(
            value = "",
            onValueChange = {},
            onAddClick = {},
            onDateClick = {},
            onReminderClick = {},
            onListPickerClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioAddTaskBarTypingLightPreview() {
    AlkaaThemePreview {
        KuvioAddTaskBar(
            value = AddTaskPreviewText,
            onValueChange = {},
            onAddClick = {},
            onDateClick = {},
            onReminderClick = {},
            onListPickerClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioAddTaskBarRestingDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioAddTaskBar(
            value = "",
            onValueChange = {},
            onAddClick = {},
            onDateClick = {},
            onReminderClick = {},
            onListPickerClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioAddTaskBarTypingDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioAddTaskBar(
            value = AddTaskPreviewText,
            onValueChange = {},
            onAddClick = {},
            onDateClick = {},
            onReminderClick = {},
            onListPickerClick = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}

private const val AddTaskPreviewText = "Review design system\u2026"
