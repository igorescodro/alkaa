package com.escodro.task.presentation.detail.alarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.escodro.core.extension.format
import com.escodro.designsystem.AlkaaTheme
import com.escodro.task.R
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import java.util.Calendar

@Composable
internal fun AlarmInfo(
    date: Calendar?,
    onRemoveDate: () -> Unit,
) {
    Column {
        if (date == null) {
            NoAlarmSet()
        } else {
            AlarmSet(
                date = date,
                onRemoveClick = onRemoveDate,
            )
        }
    }
}

@Composable
internal fun AlarmIntervalSelection(
    date: Calendar?,
    alarmInterval: AlarmInterval?,
    onIntervalSelect: (AlarmInterval) -> Unit,
) {
    val (showDialog, setDialogValue) = remember { mutableStateOf(false) }
    if (date != null) {
        AlarmIntervalDialog(
            showDialog = showDialog,
            setDialogValue = setDialogValue,
            onIntervalSelect = { interval -> onIntervalSelect(interval) },
        )

        TaskDetailSectionContent(
            modifier = Modifier
                .height(56.dp)
                .clickable { setDialogValue(true) },
            imageVector = Icons.Outlined.Repeat,
            contentDescription = R.string.task_detail_cd_icon_repeat_alarm,
        ) {
            val index = alarmInterval?.index ?: 0
            Text(
                text = stringArrayResource(id = R.array.task_alarm_repeating)[index],
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun AlarmSet(date: Calendar?, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date?.format() ?: "",
            color = MaterialTheme.colorScheme.outline,
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(
                    id = R.string.task_detail_cd_icon_remove_alarm,
                ),
            )
        }
    }
}

@Composable
private fun NoAlarmSet() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.task_detail_alarm_no_alarm),
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Composable
private fun AlarmIntervalDialog(
    showDialog: Boolean,
    setDialogValue: (Boolean) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
) {
    if (!showDialog) {
        return
    }

    Dialog(onDismissRequest = { setDialogValue(false) }) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxWidth(),
        ) {
            val intervalList = stringArrayResource(id = R.array.task_alarm_repeating)
            LazyColumn(modifier = Modifier.padding(24.dp)) {
                itemsIndexed(
                    items = intervalList,
                    itemContent = { index, title ->
                        AlarmListItem(
                            title = title,
                            index = index,
                            setDialogValue = setDialogValue,
                            onIntervalSelect = onIntervalSelect,
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun AlarmListItem(
    title: String,
    index: Int,
    setDialogValue: (Boolean) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val interval =
                    AlarmInterval.values().find { it.index == index } ?: AlarmInterval.NEVER
                onIntervalSelect(interval)
                setDialogValue(false)
            },
    )
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AlarmIntervalDialogPreview() {
    AlkaaTheme {
        AlarmIntervalDialog(
            showDialog = true,
            setDialogValue = {},
            onIntervalSelect = {},
        )
    }
}
