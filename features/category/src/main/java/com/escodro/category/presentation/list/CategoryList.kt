package com.escodro.category.presentation.list

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.escodro.category.R
import com.escodro.categoryapi.model.Category
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AddFloatingButton
import com.escodro.designsystem.components.AlkaaLoadingContent
import com.escodro.designsystem.components.DefaultIconTextContent
import org.koin.androidx.compose.getViewModel
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * Alkaa Category List Section.
 *
 * @param modifier the decorator
 * @param onShowBottomSheet function to be called when the bottom sheet is shown
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryListSection(
    modifier: Modifier,
    onShowBottomSheet: (Category?) -> Unit
) {
    CategoryListLoader(
        modifier = modifier,
        onItemClick = onShowBottomSheet,
        onAddClick = { onShowBottomSheet(null) }
    )
}

@Composable
private fun CategoryListLoader(
    modifier: Modifier,
    viewModel: CategoryListViewModel = getViewModel(),
    onItemClick: (Category) -> Unit,
    onAddClick: () -> Unit
) {
    val viewState by remember(viewModel) { viewModel.loadCategories() }
        .collectAsState(initial = CategoryState.Loading)

    CategoryListScaffold(
        modifier = modifier,
        viewState = viewState,
        onItemClick = onItemClick,
        onAddClick = onAddClick
    )
}

@Composable
private fun CategoryListScaffold(
    modifier: Modifier,
    viewState: CategoryState,
    onItemClick: (Category) -> Unit,
    onAddClick: () -> Unit
) {
    BoxWithConstraints {
        val fabPosition = if (this.maxHeight > maxWidth) FabPosition.Center else FabPosition.End
        Scaffold(
            modifier = modifier.fillMaxSize(),
            floatingActionButton = {
                AddFloatingButton(
                    contentDescription = R.string.category_cd_add_category,
                    onClick = { onAddClick() }
                )
            },
            floatingActionButtonPosition = fabPosition
        ) {
            Crossfade(viewState) { state ->
                when (state) {
                    CategoryState.Loading -> AlkaaLoadingContent()
                    CategoryState.Empty -> CategoryListEmpty()
                    is CategoryState.Loaded -> CategoryListContent(state.categoryList, onItemClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Suppress("MagicNumber")
private fun CategoryListContent(categoryList: List<Category>, onItemClick: (Category) -> Unit) {
    BoxWithConstraints(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
        val cellCount: Int = max(2F, maxWidth.value / 250).roundToInt()
        LazyVerticalGrid(cells = GridCells.Fixed(cellCount)) {
            items(
                items = categoryList,
                itemContent = { category ->
                    CategoryItem(category = category, onItemClick = onItemClick)
                }
            )
        }
    }
}

@Composable
private fun CategoryItem(
    modifier: Modifier = Modifier,
    category: Category,
    onItemClick: (Category) -> Unit
) {
    Card(
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 8.dp)
            .clickable { onItemClick(category) }
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp)
        ) {
            CategoryItemIcon(category.color)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = category.name)
        }
    }
}

@Composable
private fun CategoryItemIcon(color: Int) {
    Box(contentAlignment = Alignment.Center) {
        CategoryCircleIndicator(size = 48.dp, color = color, alpha = 0.2F)
        CategoryCircleIndicator(size = 40.dp, color = color)
        Icon(
            imageVector = Icons.Default.Bookmark,
            contentDescription = stringResource(id = R.string.category_icon_cd),
            tint = MaterialTheme.colors.background
        )
    }
}

@Composable
private fun CategoryCircleIndicator(size: Dp, color: Int, alpha: Float = 1F) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .alpha(alpha)
            .background(Color(color))
    )
}

@Composable
private fun CategoryListEmpty() {
    DefaultIconTextContent(
        icon = Icons.Outlined.ThumbUp,
        iconContentDescription = R.string.category_list_cd_empty_list,
        header = R.string.category_list_header_empty
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AboutPreview() {
    AlkaaTheme {
        CategoryItem(
            category = Category(name = "Movies", color = android.graphics.Color.RED),
            onItemClick = { }
        )
    }
}
