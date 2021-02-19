package com.escodro.task.presentation.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.ui.res.stringResource
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
    state: TaskCategoryState,
    currentCategory: Long?,
    onCategoryChanged: (CategoryId) -> Unit
) {
    when (state) {
        is TaskCategoryState.Loaded -> LoadedCategoryList(
            categoryList = state.categoryList,
            currentCategory = currentCategory,
            onCategoryChanged = onCategoryChanged
        )
        TaskCategoryState.Empty -> EmptyCategoryList()
    }
}

@Composable
private fun LoadedCategoryList(
    categoryList: List<Category>,
    currentCategory: Long?,
    onCategoryChanged: (CategoryId) -> Unit
) {
    val currentItem = categoryList.find { category -> category.id == currentCategory }
    val selectedState = remember { mutableStateOf(currentItem) }
    CategoryListContent {
        LazyRow {
            items(
                items = categoryList,
                itemContent = { category ->
                    val isSelected = category == selectedState.value
                    CategoryItemChip(
                        category = category,
                        isSelected = isSelected,
                        selectedState,
                        onCategoryChanged = { id -> onCategoryChanged(CategoryId(id)) }
                    )
                }
            )
        }
    }
}

@Composable
private fun EmptyCategoryList() {
    CategoryListContent {
        Text(
            text = stringResource(id = R.string.task_detail_category_empty_list),
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
private fun CategoryListContent(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .preferredSize(56.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIcon(
            imageVector = Icons.Default.Create,
            contentDescription = R.string.task_detail_cd_icon_category
        )
        Box(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}

@Composable
private fun CategoryItemChip(
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
private fun chipBorder(isChipSelected: Boolean): BorderStroke =
    if (isChipSelected) {
        BorderStroke(1.dp, Color.Transparent)
    } else {
        BorderStroke(1.dp, SolidColor(MaterialTheme.colors.onSecondary))
    }

private fun toggleChip(
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
fun CategorySelectionListPreview() {
    val category1 = Category(name = "Groceries", color = Color.Magenta)
    val category2 = Category(name = "Books", color = Color.Cyan)
    val category3 = Category(name = "Movies", color = Color.Red)
    val categories = listOf(category1, category2, category3)

    AlkaaTheme {
        Surface(color = MaterialTheme.colors.background) {
            CategorySelection(
                state = TaskCategoryState.Loaded(categories),
                currentCategory = category2.id,
                onCategoryChanged = {}
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun CategorySelectionEmptyPreview() {
    AlkaaTheme {
        Surface(color = MaterialTheme.colors.background) {
            CategorySelection(
                state = TaskCategoryState.Empty,
                currentCategory = null,
                onCategoryChanged = {}
            )
        }
    }
}
