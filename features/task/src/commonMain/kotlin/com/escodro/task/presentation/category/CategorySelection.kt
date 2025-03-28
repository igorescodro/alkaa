package com.escodro.task.presentation.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.resources.Res
import com.escodro.resources.task_detail_category_empty_list
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun CategorySelection(
    state: CategoryState,
    currentCategory: Long?,
    onCategoryChange: (CategoryId) -> Unit,
    modifier: Modifier = Modifier,
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
    onCategoryChange: (CategoryId) -> Unit,
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
                    },
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

@Composable
private fun CategoryItemChip(
    category: Category,
    isSelected: Boolean = false,
    onSelectChange: (Long?) -> Unit,
) {
    FilterChip(
        selected = isSelected,
        label = { Text(text = category.name) },
        modifier = Modifier.padding(end = 8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(category.color),
            selectedLabelColor = MaterialTheme.colorScheme.background,
        ),
        onClick = {
            val id = if (isSelected) null else category.id
            onSelectChange(id)
        },
    )
}
