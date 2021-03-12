package com.escodro.task.presentation.detail.alarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.escodro.core.extension.format
import com.escodro.core.view.DateTimePickerDialog
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import com.escodro.theme.AlkaaTheme
import java.util.Calendar

@Composable
internal fun AlarmSelection(
    calendar: Calendar?,
    interval: AlarmInterval?,
    onAlarmUpdate: (Calendar?) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    val context = LocalContext.current
    var date by remember { mutableStateOf(calendar) }
    var alarmInterval by remember { mutableStateOf(interval) }

    Column {
        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable {
                    DateTimePickerDialog(context) { calendar ->
                        date = calendar
                        onAlarmUpdate(calendar)
                    }.show()
                },
            imageVector = Icons.Outlined.CheckCircle,
            contentDescription = R.string.task_detail_cd_icon_alarm
        ) {
            Column {
                if (date == null) {
                    NoAlarmSet()
                } else {
                    AlarmSet(date) { date = null }
                }
            }
        }

        val showDialog = remember { mutableStateOf(false) }
        if (date != null) {
            AlarmIntervalDialog(showDialog) { interval ->
                alarmInterval = interval
                onIntervalSelect(interval)
            }

            TaskDetailSectionContent(
                modifier = Modifier
                    .height(56.dp)
                    .clickable {
                        showDialog.value = true
                    },
                imageVector = Icons.Outlined.Warning,
                contentDescription = R.string.task_detail_cd_icon_repeat_alarm
            ) {
                val index = alarmInterval?.index ?: 0
                Text(
                    text = stringArrayResource(id = R.array.task_alarm_repeating)[index],
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    }
}

@Composable
private fun AlarmSet(date: Calendar?, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date?.format() ?: "",
            color = MaterialTheme.colors.onSecondary,
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(
                    id = R.string.task_detail_cd_icon_remove_alarm
                )
            )
        }
    }
}

@Composable
private fun NoAlarmSet() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.task_detail_alarm_no_alarm),
            color = MaterialTheme.colors.onSecondary
        )
    }
}

@Composable
private fun AlarmIntervalDialog(
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    if (showDialog.value.not()) {
        return
    }

    Dialog(onDismissRequest = { showDialog.value = false }) {
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxWidth()
        ) {
            val intervalList = stringArrayResource(id = R.array.task_alarm_repeating)
            LazyColumn(modifier = Modifier.padding(24.dp)) {
                itemsIndexed(
                    items = intervalList,
                    itemContent = { index, title ->
                        AlarmListItem(
                            title = title,
                            index = index,
                            showDialog = showDialog,
                            onIntervalSelect = onIntervalSelect
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun AlarmListItem(
    title: String,
    index: Int,
    showDialog: MutableState<Boolean>,
    onIntervalSelect: (AlarmInterval) -> Unit
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val interval =
                    AlarmInterval
                        .values()
                        .find { it.index == index } ?: AlarmInterval.NEVER
                onIntervalSelect(interval)
                showDialog.value = false
            },
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmSetSelectionPreview() {
    Surface(color = MaterialTheme.colors.background) {
        AlkaaTheme {
            AlarmSelection(
                Calendar.getInstance(),
                AlarmInterval.WEEKLY,
                onAlarmUpdate = {},
                onIntervalSelect = {}
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmNotSetSelectionPreview() {
    Surface(color = MaterialTheme.colors.background) {
        AlkaaTheme {
            AlarmSelection(
                null,
                AlarmInterval.NEVER,
                onAlarmUpdate = {},
                onIntervalSelect = {}
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmIntervalDialogPreview() {
    AlkaaTheme {
        AlarmIntervalDialog(
            showDialog = mutableStateOf(true),
            onIntervalSelect = {}
        )
    }
}
