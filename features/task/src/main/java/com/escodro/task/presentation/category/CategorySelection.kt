package com.escodro.task.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.task.R
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
    categoryList: ImmutableList<Category>,
    currentCategory: Long?,
    onCategoryChange: (CategoryId) -> Unit
) {
    val currentItem = categoryList.find { category -> category.id == currentCategory }
    var selectedState by remember { mutableStateOf(currentItem?.id) }
    LazyRow {
        items(
            items = categoryList,
            itemContent = { category ->
                CategoryItemChip(
                    category = category,
                    isSelected = selectedState == category.id,
                    onSelectChange = { id ->
                        selectedState = id
                        onCategoryChange(CategoryId(id))
                    }
                )
            }
        )
    }
}

@Composable
private fun EmptyCategoryList() {
    Text(
        text = stringResource(id = R.string.task_detail_category_empty_list),
        color = MaterialTheme.colorScheme.outline
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryItemChip(
    category: Category,
    isSelected: Boolean = false,
    onSelectChange: (Long?) -> Unit
) {
    FilterChip(
        selected = isSelected,
        label = { Text(text = category.name) },
        modifier = Modifier.padding(end = 8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(category.color),
            selectedLabelColor = MaterialTheme.colorScheme.background
        ),
        onClick = {
            val id = if (isSelected) null else category.id
            onSelectChange(id)
        }
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun CategorySelectionListPreview() {
    val category1 = Category(name = "Groceries", color = android.graphics.Color.BLUE)
    val category2 = Category(name = "Books", color = android.graphics.Color.RED)
    val category3 = Category(name = "Movies", color = android.graphics.Color.GREEN)
    val categories = persistentListOf(category1, category2, category3)

    AlkaaTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
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
        Surface(color = MaterialTheme.colorScheme.background) {
            CategorySelection(
                state = CategoryState.Empty,
                currentCategory = null,
                onCategoryChange = {}
            )
        }
    }
}
