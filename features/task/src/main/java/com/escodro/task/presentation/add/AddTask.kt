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
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.task.R
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.temp.getViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AddTaskSection(sheetState: ModalBottomSheetState) {
    AddTaskLoader(sheetState = sheetState)
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
internal fun AddTaskLoader(
    sheetState: ModalBottomSheetState,
    viewModel: AddTaskViewModel = getViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        OutlinedTextField(
            label = { Text(text = stringResource(id = R.string.task_add_label)) },
            value = textState.value,
            onValueChange = { text -> textState.value = text },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                viewModel.addTask(textState.value.text)
                coroutineScope.launch { sheetState.hide() }
                textState.value = TextFieldValue()
            }
        ) {
            Text(stringResource(id = R.string.task_add_save))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun TaskListScaffoldError() {
    val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Expanded)
    AlkaaTheme {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(256.dp)
                .background(MaterialTheme.colors.background)
        ) {
            AddTaskSection(sheetState = state)
        }
    }
}
