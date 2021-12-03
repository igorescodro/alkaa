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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.category.R
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.AlkaaTheme
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.AlkaaInputTextField
import com.escodro.designsystem.components.DialogArguments
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Category Bottom Sheet.
 */
@Composable
fun CategoryBottomSheet(category: Category?, onHideBottomSheet: () -> Unit) {
    val colorList = CategoryColors.values().map { it.value }

    val editCategory = category ?: Category(
        name = "",
        color = CategoryColors.values()[0].value.toArgb()
    )

    var bottomContent by rememberSaveable(category) {
        mutableStateOf(CategoryBottomSheetState(editCategory))
    }

    CategorySheetLoader(
        colorList = colorList,
        bottomSheetState = bottomContent,
        onHideBottomSheet = {
            onHideBottomSheet()
            bottomContent = CategoryBottomSheetState(editCategory)
        }
    )
}

@Composable
private fun CategorySheetLoader(
    addViewModel: CategoryAddViewModel = getViewModel(),
    editViewModel: CategoryEditViewModel = getViewModel(),
    bottomSheetState: CategoryBottomSheetState,
    colorList: List<Color>,
    onHideBottomSheet: () -> Unit,
) {
    val onSaveCategory: (CategoryBottomSheetState) -> Unit = if (bottomSheetState.isEditing()) {
        { editCategory -> editViewModel.updateCategory(editCategory.toCategory()) }
    } else {
        { newCategory -> addViewModel.addCategory(newCategory.toCategory()) }
    }

    CategorySheetContent(
        colorList = colorList,
        state = bottomSheetState,
        onCategoryChange = { updatedState ->
            onSaveCategory(updatedState)
            onHideBottomSheet()
        },
        onCategoryRemove = { category ->
            editViewModel.deleteCategory(category)
            onHideBottomSheet()
        }
    )
}

@Composable
@Suppress("MagicNumber")
private fun CategorySheetContent(
    state: CategoryBottomSheetState,
    colorList: List<Color>,
    onCategoryChange: (CategoryBottomSheetState) -> Unit,
    onCategoryRemove: (Category) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceAround) {
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
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "")
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
    colorList: List<Color>,
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
        colors = ButtonDefaults.buttonColors(backgroundColor = colorState.value),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(id = R.string.category_sheet_save),
            color = MaterialTheme.colors.background
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
                    .background(MaterialTheme.colors.background)
            )
        }
    }
}

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
                colorList = CategoryColors.values().map { it.value },
                onCategoryChange = {},
                onCategoryRemove = {}
            )
        }
    }
}
