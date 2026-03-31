package com.escodro.task.presentation.detail.checklist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.escodro.resources.Res
import com.escodro.resources.task_detail_checklist_add_item
import com.escodro.resources.task_detail_checklist_cd_delete_item
import com.escodro.resources.task_detail_checklist_label
import com.escodro.task.model.ChecklistItem
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun Checklist(
    checklistItems: List<ChecklistItem>,
    onAdd: (String) -> Unit,
    onUpdate: (ChecklistItem) -> Unit,
    onDelete: (ChecklistItem) -> Unit,
) {
    TaskDetailSectionContent(
        title = stringResource(Res.string.task_detail_checklist_label),
        modifier = Modifier.testTag("checklist_section"),
    ) {
        Column {
            checklistItems.forEach { item ->
                ChecklistItemRow(item = item, onUpdate = onUpdate, onDelete = onDelete)
            }
            ChecklistAddItem(onAdd = onAdd)
        }
    }
}

@Composable
private fun ChecklistItemRow(
    item: ChecklistItem,
    onUpdate: (ChecklistItem) -> Unit,
    onDelete: (ChecklistItem) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { isChecked -> onUpdate(item.copy(isCompleted = isChecked)) },
            modifier = Modifier.testTag("checkbox_${item.title}"),
        )

        var text by remember(item.title) { mutableStateOf(item.title) }

        TextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .weight(1f)
                .testTag("item_text_${item.title}")
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && text != item.title && text.isNotBlank()) {
                        onUpdate(item.copy(title = text))
                    }
                },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (text.isNotBlank()) {
                    onUpdate(item.copy(title = text))
                }
            })
        )

        IconButton(
            modifier = Modifier.testTag("delete_button_${item.title}"),
            onClick = { onDelete(item) }
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = stringResource(Res.string.task_detail_checklist_cd_delete_item),
            )
        }
    }
}

@Composable
private fun ChecklistAddItem(onAdd: (String) -> Unit) {
    var newItemText by remember { mutableStateOf("") }

    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = newItemText,
            onValueChange = { newItemText = it },
            placeholder = { Text(stringResource(Res.string.task_detail_checklist_add_item)) },
            modifier = Modifier
                .weight(1f)
                .testTag("checklist_add_item_text"),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (newItemText.isNotBlank()) {
                    onAdd(newItemText)
                    newItemText = ""
                }
            })
        )

        IconButton(
            modifier = Modifier.testTag("checklist_add_item_button"),
            onClick = {
                if (newItemText.isNotBlank()) {
                    onAdd(newItemText)
                    newItemText = ""
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.task_detail_checklist_add_item),
            )
        }
    }
}
