package com.escodro.task.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.escodro.task.model.Category

@Composable
internal fun LeadingIcon(
    imageVector: ImageVector,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = stringResource(id = contentDescription),
        tint = MaterialTheme.colors.onSurface.copy(alpha = TrailingLeadingAlpha),
        modifier = modifier
    )
}

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
            modifier = Modifier
                .semantics { chipName = category.name ?: "" }
                .toggleable(
                    value = isSelected,
                    role = Role.RadioButton,
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

internal val ChipNameKey = SemanticsPropertyKey<String>("ChipNameKey")
private var SemanticsPropertyReceiver.chipName by ChipNameKey

private const val TrailingLeadingAlpha = 0.54f
