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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Composable function that represents a filter chip for a category item.
 */
@Suppress("LongParameterList")
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
            val validId = if (isSelected) null else id
            onSelectChange(validId)
        },
        modifier = modifier
            .innerBorder()
            .dropShadow(
                shape = MaterialTheme.shapes.extraLarge,
                shadow = Shadow(
                    radius = 6.dp,
                    spread = 1.dp,
                    color = if (isSelected) {
                        color
                    } else {
                        Color.Transparent
                    },
                    offset = DpOffset(0.dp, 0.dp),
                    alpha = 0.5f,
                ),
            )
            .height(40.dp),
    )
}

@Composable
private fun Modifier.innerBorder(
    color: Color = MaterialTheme.colorScheme.surface,
    strokeWidth: Float = 1f,
): Modifier = this.drawWithContent {
    val strokeWidthPx: Float = strokeWidth.dp.toPx()
    val padding: Float = 1.dp.toPx()

    drawContent()
    drawRoundRect(
        color = color.copy(alpha = 0.5f),
        style = Stroke(width = strokeWidthPx),
        cornerRadius = CornerRadius(x = size.height / 2, y = size.height / 2),
        topLeft = Offset(padding, padding),
        size = Size(
            width = size.width - padding * 2,
            height = size.height - padding * 2,
        ),
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
