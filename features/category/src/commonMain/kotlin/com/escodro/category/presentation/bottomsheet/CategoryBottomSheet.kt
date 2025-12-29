package com.escodro.category.presentation.bottomsheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.components.dialog.AlkaaDialog
import com.escodro.designsystem.components.dialog.DialogArguments
import com.escodro.designsystem.components.menu.CategoryColorMenu
import com.escodro.designsystem.components.textfield.AlkaaInputTextField
import com.escodro.resources.Res
import com.escodro.resources.category_add_label
import com.escodro.resources.category_cd_remove_category
import com.escodro.resources.category_dialog_remove_cancel
import com.escodro.resources.category_dialog_remove_confirm
import com.escodro.resources.category_dialog_remove_text
import com.escodro.resources.category_dialog_remove_title
import com.escodro.resources.category_sheet_save
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa Category Bottom Sheet.
 */
@Composable
internal fun CategoryBottomSheet(categoryId: Long, onHideBottomSheet: () -> Unit) {
    val colorList = CategoryColors.entries.map { it.value }.toImmutableList()
    if (categoryId == 0L) {
        CategoryNewSheetLoader(
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet,
        )
    } else {
        CategoryEditSheetLoader(
            categoryId = categoryId,
            colorList = colorList,
            onHideBottomSheet = onHideBottomSheet,
        )
    }
}

@Composable
private fun CategoryNewSheetLoader(
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    addViewModel: CategoryAddViewModel = koinInject(),
) {
    var category by rememberSaveable(addViewModel) { mutableStateOf(emptyCategory()) }

    CategoryBottomSheet(
        colorList = colorList,
        category = category,
        onHideBottomSheet = onHideBottomSheet,
        onCategoryUpdate = { updatedCategory ->
            category = updatedCategory
        },
        onCategorySave = { newCategory ->
            addViewModel.addCategory(newCategory)
        },
    )
}

@Composable
private fun CategoryEditSheetLoader(
    categoryId: Long,
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    editViewModel: CategoryEditViewModel = koinInject(),
) {
    val categoryState by remember(editViewModel, categoryId) {
        editViewModel.loadCategory(categoryId = categoryId)
    }.collectAsState(initial = CategorySheetState.Empty)

    var category by rememberCategory(categoryState)

    CategoryBottomSheet(
        colorList = colorList,
        category = category,
        onHideBottomSheet = onHideBottomSheet,
        onCategoryUpdate = { updatedCategory ->
            category = updatedCategory
        },
        onCategorySave = { updateCategory ->
            editViewModel.updateCategory(updateCategory)
        },
        onCategoryRemove = { categoryToRemove ->
            editViewModel.deleteCategory(categoryToRemove)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("MagicNumber")
private fun CategoryBottomSheet(
    category: Category,
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    onCategoryUpdate: (Category) -> Unit,
    onCategorySave: (Category) -> Unit,
    onCategoryRemove: (Category) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = {
            scope.launch { sheetState.hide() }
            onHideBottomSheet()
        },
    ) {
        CategoryBottomSheetContent(
            category = category,
            onCategoryRemove = onCategoryRemove,
            onHideBottomSheet = onHideBottomSheet,
            colorList = colorList,
            onCategoryUpdate = onCategoryUpdate,
            onCategorySave = onCategorySave,
        )
    }
}

@Composable
private fun CategoryBottomSheetContent(
    category: Category,
    onCategoryRemove: (Category) -> Unit,
    onCategoryUpdate: (Category) -> Unit,
    onCategorySave: (Category) -> Unit,
    onHideBottomSheet: () -> Unit,
    colorList: ImmutableList<Color>,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            var openDialog by rememberSaveable { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }

            LaunchedEffect(Unit) {
                delay(300)
                focusRequester.requestFocus()
            }

            RemoveCategoryDialog(
                categoryName = category.name,
                isDialogOpen = openDialog,
                onCloseDialog = { openDialog = false },
                onActionConfirm = {
                    onCategoryRemove(category)
                    onHideBottomSheet()
                },
            )

            AlkaaInputTextField(
                label = stringResource(Res.string.category_add_label),
                text = category.name,
                onTextChange = { name ->
                    onCategoryUpdate(category.copy(name = name))
                },
                modifier = Modifier
                    .weight(5F)
                    .focusRequester(focusRequester),
            )
            if (category.isEditing()) {
                IconButton(
                    onClick = { openDialog = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    modifier = Modifier
                        .height(64.dp)
                        .weight(1F),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = stringResource(Res.string.category_cd_remove_category),
                    )
                }
            }
        }

        CategoryColorMenu(
            colorList = colorList,
            value = Color(category.color),
            onColorChange = { color ->
                onCategoryUpdate(category.copy(color = color.toArgb()))
            },
            modifier = Modifier.padding(top = 16.dp),
        )

        CategorySaveButton(
            currentColor = Color(category.color),
            onClick = {
                onCategorySave(category)
                onHideBottomSheet()
            },
        )
    }
}

@Composable
private fun RemoveCategoryDialog(
    categoryName: String,
    isDialogOpen: Boolean,
    onCloseDialog: () -> Unit,
    onActionConfirm: () -> Unit,
) {
    val arguments = DialogArguments(
        title = stringResource(Res.string.category_dialog_remove_title),
        text = stringResource(Res.string.category_dialog_remove_text, categoryName),
        confirmText = stringResource(Res.string.category_dialog_remove_confirm),
        dismissText = stringResource(Res.string.category_dialog_remove_cancel),
        onConfirmAction = {
            onActionConfirm()
            onCloseDialog()
        },
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = onCloseDialog,
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
            .height(48.dp),
    ) {
        Text(
            text = stringResource(Res.string.category_sheet_save),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
private fun rememberCategory(categorySheetState: CategorySheetState): MutableState<Category> =
    remember(categorySheetState) {
        mutableStateOf(
            when (categorySheetState) {
                CategorySheetState.Empty -> emptyCategory()
                is CategorySheetState.Loaded -> categorySheetState.category
            },
        )
    }

private fun emptyCategory() = Category(
    name = "",
    color = CategoryColors.entries[0].value.toArgb(),
)
