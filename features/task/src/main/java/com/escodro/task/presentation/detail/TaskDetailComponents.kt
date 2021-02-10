package com.escodro.task.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.escodro.task.model.Category

@Composable
internal fun CategoryItemChip(
    category: Category,
    isSelected: Boolean = false,
    selectedState: MutableState<Category?>,
    onCategoryChanged: (Long?) -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        shape = MaterialTheme.shapes.small,
        color = if (isSelected) category.color else Color.White,
        border = chipBorder(isSelected)
    ) {
        Row(
            modifier = Modifier.toggleable(
                value = isSelected,
                onValueChange = {
                    val newCategory = toggleChip(selectedState, category)
                    selectedState.value = newCategory
                    onCategoryChanged(newCategory?.id)
                }
            )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                color = if (isSelected) Color.White else MaterialTheme.colors.onSecondary,
                text = category.name ?: ""
            )
        }
    }
}

@Composable
internal fun chipBorder(isChipSelected: Boolean): BorderStroke =
    if (isChipSelected) {
        BorderStroke(1.dp, Color.Transparent)
    } else {
        BorderStroke(1.dp, SolidColor(MaterialTheme.colors.onSecondary))
    }

internal fun toggleChip(
    selectedState: MutableState<Category?>,
    category: Category
): Category? =
    if (selectedState.value == category) {
        null
    } else {
        category
    }
