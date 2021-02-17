package com.escodro.task.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.task.R
import com.escodro.task.model.Category
import com.escodro.theme.AlkaaTheme

@Composable
internal fun CategorySelection(
    categories: List<Category>,
    currentCategory: Long?,
    onCategoryChanged: (Long?) -> Unit
) {
    val currentItem = categories.find { category -> category.id == currentCategory }
    val selectedState = remember { mutableStateOf(currentItem) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .preferredSize(56.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIcon(
            imageVector = Icons.Default.Create,
            contentDescription = R.string.task_detail_cd_icon_category
        )
        LazyRow(modifier = Modifier.padding(start = 16.dp)) {
            items(
                items = categories,
                itemContent = { category ->
                    val isSelected = category == selectedState.value
                    CategoryItemChip(
                        category = category,
                        isSelected = isSelected,
                        selectedState,
                        onCategoryChanged = onCategoryChanged
                    )
                }
            )
        }
    }
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

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun CategorySelectionPreview() {
    val category1 = Category(name = "Groceries", color = Color.Magenta)
    val category2 = Category(name = "Books", color = Color.Cyan)
    val category3 = Category(name = "Movies", color = Color.Red)
    val categories = listOf(category1, category2, category3)

    AlkaaTheme {
        Surface(color = MaterialTheme.colors.background) {
            CategorySelection(
                categories = categories,
                currentCategory = category2.id,
                onCategoryChanged = {}
            )
        }
    }
}
