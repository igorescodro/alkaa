package com.escodro.designsystem.components.chip

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
        label = {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (isSelected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Normal
                },
            )
        },
        modifier = modifier.height(40.dp).padding(end = 8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = color,
            selectedLabelColor = MaterialTheme.colorScheme.background,
        ),
        shape = MaterialTheme.shapes.extraLarge,
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) {
                Color.Black.copy(alpha = 0.2f)
            } else {
                MaterialTheme.colorScheme.outline
            },
        ),
        leadingIcon = {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.size(16.dp),
                )
            }
        },
        onClick = {
            val id = if (isSelected) null else id
            onSelectChange(id)
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun CategoryItemChipPreview(
    @PreviewParameter(CategoryItemChipPreviewProvider::class) category: Category,
) {
    AlkaaThemePreview {
        CategoryItemChip(
            id = category.id,
            name = category.name,
            color = category.color,
            isSelected = category.isSelected,
            onSelectChange = {},
            modifier = Modifier.padding(8.dp),
        )
    }
}

private class CategoryItemChipPreviewProvider : PreviewParameterProvider<Category> {
    override val values: Sequence<Category> = sequenceOf(
        Category(
            id = 1L,
            name = "Personal",
            color = Color(0xFF62A7E0),
            isSelected = true,
        ),
        Category(
            id = 2L,
            name = "Work",
            color = Color(0xFF4CB27C),
            isSelected = false,
        ),
        Category(
            id = 3L,
            name = "Shopping",
            color = Color(0xFFF98847),
            isSelected = true,
        ),
    )
}

private data class Category(
    val id: Long,
    val name: String,
    val color: Color,
    val isSelected: Boolean,
)
