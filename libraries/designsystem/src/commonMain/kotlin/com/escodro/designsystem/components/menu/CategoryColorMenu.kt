package com.escodro.designsystem.components.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.semantics.color
import com.escodro.designsystem.theme.AlkaaThemePreview
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that displays a horizontal list of selectable colors.
 *
 * @param colorList the list of colors available for selection
 * @param value the currently selected color
 * @param onColorChange a callback triggered when a new color is selected
 * @param modifier the modifier applied to the LazyRow that contains the color items
 */
@Composable
fun CategoryColorMenu(
    colorList: ImmutableList<Color>,
    value: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(modifier = modifier.fillMaxWidth()) {
        items(
            items = colorList,
            itemContent = { color ->
                val optionSelected = color == value
                CategoryColorItem(color, optionSelected, onClick = { onColorChange(color) })
            },
        )
    }
}

@Composable
private fun CategoryColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = color)
                .semantics { this.color = color }
                .selectable(
                    role = Role.RadioButton,
                    selected = isSelected,
                    onClick = onClick,
                ),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CategoryColorMenuPreview() {
    val colors =
        listOf(
            Color(0xFFE57373),
            Color(0xFFF06292),
            Color(0xFFBA68C8),
            Color(0xFF9575CD),
            Color(0xFF64B5F6),
        ).toPersistentList()

    val currentColor = remember { mutableStateOf(colors[0]) }

    AlkaaThemePreview {
        CategoryColorMenu(
            colorList = colors,
            value = currentColor.value,
            onColorChange = { currentColor.value = it },
            modifier = Modifier.padding(16.dp),
        )
    }
}
