package com.escodro.task.presentation.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.task.R
import com.escodro.task.presentation.detail.main.CategoryId

@Composable
internal fun CategorySelection(
    state: CategoryState,
    currentCategory: Long?,
    onCategoryChange: (CategoryId) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(56.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        when (state) {
            CategoryState.Loading -> AlkaaLoadingContent()
            is CategoryState.Loaded -> LoadedCategoryList(
                categoryList = state.categoryList,
                currentCategory = currentCategory,
                onCategoryChange = onCategoryChange
            )
            CategoryState.Empty -> EmptyCategoryList()
        }
    }
}

@Composable
private fun LoadedCategoryList(
    categoryList: List<Category>,
    currentCategory: Long?,
    onCategoryChange: (CategoryId) -> Unit
) {
    val currentItem = categoryList.find { category -> category.id == currentCategory }
    val selectedState = remember { mutableStateOf(currentItem) }
    LazyRow {
        items(
            items = categoryList,
            itemContent = { category ->
                val isSelected = category == selectedState.value
                CategoryItemChip(
                    category = category,
                    isSelected = isSelected,
                    selectedState,
                    onCategoryChange = { id -> onCategoryChange(CategoryId(id)) }
                )
            }
        )
    }
}

@Composable
private fun EmptyCategoryList() {
    Text(
        text = stringResource(id = R.string.task_detail_category_empty_list),
        color = MaterialTheme.colors.onSecondary
    )
}

@Composable
private fun CategoryItemChip(
    category: Category,
    isSelected: Boolean = false,
    selectedState: MutableState<Category?>,
    onCategoryChange: (Long?) -> Unit
) {
    Surface(
        modifier = Modifier.padding(end = 8.dp),
        shape = CircleShape,
        color = if (isSelected) Color(category.color) else MaterialTheme.colors.background,
        border = chipBorder(isSelected)
    ) {
        Row(
            modifier = Modifier
                .semantics { chipName = category.name }
                .toggleable(
                    value = isSelected,
                    role = Role.RadioButton,
                    onValueChange = {
                        val newCategory = toggleChip(selectedState, category)
                        selectedState.value = newCategory
                        onCategoryChange(newCategory?.id)
                    }
                )
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                color = if (isSelected) {
                    MaterialTheme.colors.background
                } else {
                    MaterialTheme.colors.onSecondary
                },
                text = category.name
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
    val category1 = Category(name = "Groceries", color = android.graphics.Color.BLUE)
    val category2 = Category(name = "Books", color = android.graphics.Color.RED)
    val category3 = Category(name = "Movies", color = android.graphics.Color.GREEN)
    val categories = listOf(category1, category2, category3)

    AlkaaTheme {
        Surface(color = MaterialTheme.colors.background) {
            CategorySelection(
                state = CategoryState.Loaded(categories),
                currentCategory = category2.id,
                onCategoryChange = {}
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
                state = CategoryState.Empty,
                currentCategory = null,
                onCategoryChange = {}
            )
        }
    }
}
