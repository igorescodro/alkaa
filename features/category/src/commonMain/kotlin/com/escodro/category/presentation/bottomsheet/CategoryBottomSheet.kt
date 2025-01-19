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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.escodro.category.presentation.semantics.color
import com.escodro.categoryapi.model.Category
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.AlkaaInputTextField
import com.escodro.designsystem.components.DialogArguments
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
    val sheetState by rememberSaveable(addViewModel) {
        mutableStateOf(CategoryBottomSheetState(emptyCategory()))
    }

    CategoryBottomSheet(
        colorList = colorList,
        state = sheetState,
        onHideBottomSheet = onHideBottomSheet,
        onCategoryChange = { updatedState ->
            addViewModel.addCategory(updatedState.toCategory())
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

    val category = when (categoryState) {
        CategorySheetState.Empty -> emptyCategory()
        is CategorySheetState.Loaded -> (categoryState as CategorySheetState.Loaded).category
    }

    val sheetState by rememberSaveable(categoryState) {
        mutableStateOf(CategoryBottomSheetState(category))
    }

    CategoryBottomSheet(
        colorList = colorList,
        state = sheetState,
        onHideBottomSheet = onHideBottomSheet,
        onCategoryChange = { updatedState ->
            editViewModel.updateCategory(updatedState.toCategory())
        },
        onCategoryRemove = {
            editViewModel.deleteCategory(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("MagicNumber")
private fun CategoryBottomSheet(
    state: CategoryBottomSheetState,
    colorList: ImmutableList<Color>,
    onHideBottomSheet: () -> Unit,
    onCategoryChange: (CategoryBottomSheetState) -> Unit,
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
        LaunchedEffect(Unit) {
            sheetState.show()
        }
        CategoryBottomSheetContent(
            state = state,
            onCategoryRemove = onCategoryRemove,
            onHideBottomSheet = onHideBottomSheet,
            colorList = colorList,
            onCategoryChange = onCategoryChange,
        )
    }
}

@Composable
private fun CategoryBottomSheetContent(
    state: CategoryBottomSheetState,
    onCategoryRemove: (Category) -> Unit,
    onHideBottomSheet: () -> Unit,
    colorList: ImmutableList<Color>,
    onCategoryChange: (CategoryBottomSheetState) -> Unit,
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
                categoryName = state.name,
                isDialogOpen = openDialog,
                onCloseDialog = { openDialog = false },
                onActionConfirm = {
                    onCategoryRemove(state.toCategory())
                    onHideBottomSheet()
                },
            )

            AlkaaInputTextField(
                label = stringResource(Res.string.category_add_label),
                text = state.name,
                onTextChange = { state.name = it },
                modifier = Modifier
                    .weight(5F)
                    .focusRequester(focusRequester),
            )
            if (state.isEditing()) {
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

        CategoryColorSelector(
            colorList = colorList,
            value = Color(state.color),
            onColorChange = { state.color = it.toArgb() },
        )

        CategorySaveButton(
            currentColor = Color(state.color),
            onClick = {
                onCategoryChange(state)
                onHideBottomSheet()
            },
        )
    }
}

@Composable
private fun CategoryColorSelector(
    colorList: ImmutableList<Color>,
    value: Color,
    onColorChange: (Color) -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        items(
            items = colorList,
            itemContent = { color ->
                val optionSelected = color == value
                CategoryColorItem(color, optionSelected, onClick = { onColorChange(color) })
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
            color = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
private fun CategoryColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 8.dp),
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
                    onClick = onClick,
                ),
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.background),
            )
        }
    }
}

private fun emptyCategory() = Category(
    name = "",
    color = CategoryColors.entries[0].value.toArgb(),
)
