package com.escodro.category.presentation.bottomsheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.category.R
import com.escodro.category.presentation.semantics.color
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.AlkaaInputTextField
import com.escodro.designsystem.components.DialogArguments
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Category Bottom Sheet.
 */
@Composable
fun CategoryBottomSheet(categoryId: Long, onHideBottomSheet: () -> Unit) {
    val colorList = CategoryColors.values().map { it.value }.toImmutableList()
    if (categoryId == 0L) {
        CategoryNewSheetLoader(
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet
        )
    } else {
        CategoryEditSheetLoader(
            categoryId = categoryId,
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet
        )
    }
}

@Composable
private fun CategoryNewSheetLoader(
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    addViewModel: CategoryAddViewModel = getViewModel()
) {
    val sheetState by rememberSaveable(addViewModel) {
        mutableStateOf(CategoryBottomSheetState(emptyCategory()))
    }

    CategorySheetContent(
        colorList = colorList,
        state = sheetState,
        onCategoryChange = { updatedState ->
            addViewModel.addCategory(updatedState.toCategory())
            onHideBottomSheet()
        }
    )
}

@Composable
private fun CategoryEditSheetLoader(
    categoryId: Long,
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    editViewModel: CategoryEditViewModel = getViewModel()
) {
    val categoryState by remember(editViewModel, categoryId) {
        editViewModel.loadCategory(categoryId = categoryId)
    }.collectAsState(initial = CategorySheetState.Empty)

    val category = when (categoryState) {
        CategorySheetState.Empty -> emptyCategory()
        is CategorySheetState.Loaded -> (categoryState as CategorySheetState.Loaded).category
    }

    val sheetState by rememberSaveable(categoryState) {
        mutableStateOf(CategoryBottomSheetState(category))
    }

    CategorySheetContent(
        colorList = colorList,
        state = sheetState,
        onCategoryChange = { updatedState ->
            editViewModel.updateCategory(updatedState.toCategory())
            onHideBottomSheet()
        },
        onCategoryRemove = {
            editViewModel.deleteCategory(it)
            onHideBottomSheet()
        }
    )
}

@Composable
@Suppress("MagicNumber")
private fun CategorySheetContent(
    state: CategoryBottomSheetState,
    colorList: ImmutableList<Color>,
    onCategoryChange: (CategoryBottomSheetState) -> Unit,
    onCategoryRemove: (Category) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .background(MaterialTheme.colorScheme.surface) // Accompanist does not support M3 yet
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var openDialog by rememberSaveable { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                delay(300)
                focusRequester.requestFocus()
            }

            RemoveCategoryDialog(
                categoryName = state.name,
                isDialogOpen = openDialog,
                onCloseDialog = { openDialog = false },
                onActionConfirm = { onCategoryRemove(state.toCategory()) }
            )

            AlkaaInputTextField(
                label = stringResource(id = R.string.category_add_label),
                text = state.name,
                onTextChange = { state.name = it },
                modifier = Modifier
                    .weight(5F)
                    .focusRequester(focusRequester)
            )
            if (state.isEditing()) {
                IconButton(
                    onClick = { openDialog = true },
                    modifier = Modifier
                        .height(64.dp)
                        .weight(1F)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(id = R.string.category_cd_remove_category)
                    )
                }
            }
        }

        CategoryColorSelector(
            colorList = colorList,
            value = Color(state.color),
            onColorChange = { state.color = it.toArgb() }
        )

        CategorySaveButton(
            currentColor = Color(state.color),
            onClick = { onCategoryChange(state) }
        )
    }
}

@Composable
private fun CategoryColorSelector(
    colorList: ImmutableList<Color>,
    value: Color,
    onColorChange: (Color) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        items(
            items = colorList,
            itemContent = { color ->
                val optionSelected = color == value
                CategoryColorItem(color, optionSelected, onClick = { onColorChange(color) })
            }
        )
    }
}

@Composable
private fun RemoveCategoryDialog(
    categoryName: String,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onActionConfirm: () -> Unit
) {
    val arguments = DialogArguments(
        title = stringResource(id = R.string.category_dialog_remove_title),
        text = stringResource(id = R.string.category_dialog_remove_text, categoryName),
        confirmText = stringResource(id = R.string.category_dialog_remove_confirm),
        dismissText = stringResource(id = R.string.category_dialog_remove_cancel),
        onConfirmAction = {
            onActionConfirm()
            onCloseDialog()
        }
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog
    )
}

@Composable
private fun CategorySaveButton(currentColor: Color, onClick: () -> Unit) {
    val colorState = animateColorAsState(targetValue = currentColor)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = colorState.value),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(id = R.string.category_sheet_save),
            color = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
private fun CategoryColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = color)
                .semantics { this.color = color }
                .selectable(
                    role = Role.RadioButton,
                    selected = isSelected,
                    onClick = onClick
                )
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}

private fun emptyCategory() = Category(
    name = "",
    color = CategoryColors.values()[0].value.toArgb()
)

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun CategorySheetContentPreview() {
    AlkaaTheme {
        Surface(modifier = Modifier.height(256.dp)) {
            val category = Category(id = 1L, name = "Movies", color = android.graphics.Color.YELLOW)
            val state = CategoryBottomSheetState(category)
            CategorySheetContent(
                state = state,
                colorList = CategoryColors.values().map { it.value }.toImmutableList(),
                onCategoryChange = {},
                onCategoryRemove = {}
            )
        }
    }
}
