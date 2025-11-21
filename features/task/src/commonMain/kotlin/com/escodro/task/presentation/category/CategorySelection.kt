package com.escodro.task.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.chip.CategoryItemChip
import com.escodro.designsystem.components.content.AlkaaLoadingContent
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.task_detail_category_empty_list
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun CategorySelection(
    state: CategoryState,
    currentCategory: Long?,
    onCategoryChange: (CategoryId) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Box(
        modifier = modifier.height(56.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        when (state) {
            CategoryState.Loading -> AlkaaLoadingContent()

            is CategoryState.Loaded -> LoadedCategoryList(
                categoryList = state.categoryList,
                currentCategory = currentCategory,
                contentPadding = contentPadding,
                onCategoryChange = onCategoryChange,
            )

            CategoryState.Empty -> EmptyCategoryList()
        }
    }
}

@Composable
private fun LoadedCategoryList(
    categoryList: ImmutableList<Category>,
    currentCategory: Long?,
    contentPadding: PaddingValues,
    onCategoryChange: (CategoryId) -> Unit,
) {
    val state = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val currentItem = categoryList.find { category -> category.id == currentCategory }
    var selectedState by remember { mutableStateOf(currentItem?.id) }

    LazyRow(
        state = state,
        contentPadding = contentPadding,
    ) {
        item {
            val selectedItem =
                categoryList.find { category -> category.id == selectedState } ?: return@item
            CategoryItemChip(
                id = selectedItem.id,
                name = selectedItem.name,
                color = Color(selectedItem.color),
                isSelected = true,
                onSelectChange = { id ->
                    coroutineScope.launch { state.animateScrollToItem(0) }
                    selectedState = id
                    onCategoryChange(CategoryId(id))
                },
                modifier = Modifier.padding(end = 8.dp),
            )
        }
        items(
            items = categoryList.filter { category -> category.id != selectedState },
            itemContent = { category ->
                CategoryItemChip(
                    id = category.id,
                    name = category.name,
                    color = Color(category.color),
                    isSelected = selectedState == category.id,
                    onSelectChange = { id ->
                        coroutineScope.launch { state.animateScrollToItem(0) }
                        selectedState = id
                        onCategoryChange(CategoryId(id))
                    },
                    modifier = Modifier.padding(end = 8.dp),
                )
            },
        )
    }
}

@Composable
private fun EmptyCategoryList() {
    Text(
        text = stringResource(Res.string.task_detail_category_empty_list),
        color = MaterialTheme.colorScheme.outline,
    )
}

@Preview(showBackground = true)
@Composable
private fun CategorySelectionPreview() {
    AlkaaThemePreview {
        val categoryList = listOf(
            Category(id = 1L, name = "Personal", color = Color(0xFF62A7E0).toArgb()),
            Category(id = 2L, name = "Work", color = Color(0xFF4CB27C).toArgb()),
            Category(id = 3L, name = "Shopping", color = Color(0xFFF98847).toArgb()),
        ).toImmutableList()

        var currentCategory by remember { mutableStateOf<CategoryId?>(CategoryId(1L)) }

        CategorySelection(
            state = CategoryState.Loaded(categoryList = categoryList),
            currentCategory = currentCategory?.value,
            onCategoryChange = { currentCategory = it },
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 8.dp),
        )
    }
}
