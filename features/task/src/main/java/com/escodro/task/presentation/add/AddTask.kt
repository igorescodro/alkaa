package com.escodro.task.presentation.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.categoryapi.presentation.CategoryListViewModel
import com.escodro.categoryapi.presentation.CategoryState
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.R
import com.escodro.task.presentation.category.CategorySelection
import com.escodro.task.presentation.detail.main.CategoryId
import kotlinx.coroutines.delay
import org.koin.androidx.compose.getViewModel

/**
 * Alkaa Add Task Bottom Sheet.
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddTaskBottomSheet(onHideBottomSheet: () -> Unit) {
    AddTaskLoader(onHideBottomSheet = onHideBottomSheet)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AddTaskLoader(
    addTaskViewModel: AddTaskViewModel = getViewModel(),
    categoryViewModel: CategoryListViewModel = getViewModel(),
    onHideBottomSheet: () -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        var taskInputText by remember { mutableStateOf("") }
        val categoryState by remember(categoryViewModel) { categoryViewModel }.loadCategories()
            .collectAsState(initial = CategoryState.Empty)
        val currentCategory = remember { mutableStateOf<CategoryId?>(null) }
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            delay(300)
            focusRequester.requestFocus()
        }

        TaskInputTextField(
            text = taskInputText,
            onTextChange = { text -> taskInputText = text },
            modifier = Modifier.focusRequester(focusRequester)
        )

        CategorySelection(
            state = categoryState,
            currentCategory = null,
            onCategoryChange = { categoryId -> currentCategory.value = categoryId }
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            onClick = {
                addTaskViewModel.addTask(taskInputText, currentCategory.value)
                taskInputText = ""
                onHideBottomSheet()
            }
        ) {
            Text(stringResource(id = R.string.task_add_save))
        }
    }
}

@Composable
private fun TaskInputTextField(
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        label = { Text(text = stringResource(id = R.string.task_add_label)) },
        value = text,
        onValueChange = onTextChange,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldError() {
    AlkaaTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(MaterialTheme.colors.background)
        ) {
            AddTaskBottomSheet(onHideBottomSheet = {})
        }
    }
}
