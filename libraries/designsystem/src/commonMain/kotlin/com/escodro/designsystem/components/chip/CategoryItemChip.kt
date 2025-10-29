package com.escodro.designsystem.components.chip

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

/**
 * Composable function that represents a filter chip for a category item.
 *
 * @param id The unique identifier of the category item.
 * @param name The display name of the category item.
 * @param color The background color of the chip when selected.
 * @param isSelected A boolean indicating whether the chip is selected. Defaults to false.
 * @param onSelectChange A lambda function to handle the selection change, passing the ID
 *                       of the selected category or null if unselected.
 */
@Composable
fun CategoryItemChip(
    id: Long,
    name: String,
    color: Color,
    onSelectChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    FilterChip(
        selected = isSelected,
        label = { Text(text = name) },
        modifier = modifier.padding(end = 8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = MaterialTheme.colorScheme.background,
        ),
        onClick = {
            val id = if (isSelected) null else id
            onSelectChange(id)
        },
    )
}

@Preview
@Composable
private fun CategoryItemChipPreview(
    @PreviewParameter(CategoryItemChipPreviewProvider::class) category: Category,
) {
    AlkaaThemePreview {
        CategoryItemChip(
            id = category.id,
            name = category.name,
            color = category.color,
            isSelected = true,
            onSelectChange = {},
        )
    }
}

private class CategoryItemChipPreviewProvider : PreviewParameterProvider<Category> {
    override val values: Sequence<Category> = sequenceOf(
        Category(
            id = 1L,
            name = "Work",
            color = Color(0xFF6200EE),
        ),
        Category(
            id = 2L,
            name = "Personal",
            color = Color(0xFF03DAC5),
        ),
        Category(
            id = 3L,
            name = "Shopping",
            color = Color(0xFFFF5722),
        ),
    )
}

private data class Category(
    val id: Long,
    val name: String,
    val color: Color,
)
