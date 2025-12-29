package com.escodro.task.presentation.detail.alarm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.task_detail_alarm_no_alarm
import com.escodro.resources.task_detail_cd_icon_remove_alarm
import com.escodro.resources.task_detail_cd_icon_repeat_alarm
import com.escodro.task.extension.format
import com.escodro.task.model.AlarmInterval
import com.escodro.task.presentation.detail.TaskDetailSectionContent
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AlarmInfo(
    date: LocalDateTime?,
    onRemoveDate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
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
    date: LocalDateTime?,
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
            contentDescription = stringResource(Res.string.task_detail_cd_icon_repeat_alarm),
        ) {
            val index = alarmInterval?.index ?: 0
            val interval = AlarmInterval.entries.find { it.index == index } ?: AlarmInterval.NEVER
            Text(
                text = stringResource(interval.title),
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
private fun AlarmSet(date: LocalDateTime?, onRemoveClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(
            text = date?.format().orEmpty(),
            color = MaterialTheme.colorScheme.outline,
        )
        IconButton(onClick = onRemoveClick) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(Res.string.task_detail_cd_icon_remove_alarm),
            )
        }
    }
}

@Composable
private fun NoAlarmSet() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(Res.string.task_detail_alarm_no_alarm),
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
            val intervalList = remember { AlarmInterval.entries.toTypedArray() }
            LazyColumn(modifier = Modifier.padding(24.dp)) {
                items(intervalList) { interval ->
                    AlarmListItem(
                        title = stringResource(interval.title),
                        index = interval.index,
                        setDialogValue = setDialogValue,
                        onIntervalSelect = onIntervalSelect,
                    )
                }
            }
        }
    }
}

@Composable
private fun AlarmListItem(
    title: String,
    index: Int?,
    setDialogValue: (Boolean) -> Unit,
    onIntervalSelect: (AlarmInterval) -> Unit,
) {
    Text(
        text = title,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val interval =
                    AlarmInterval.entries.find { it.index == index } ?: AlarmInterval.NEVER
                onIntervalSelect(interval)
                setDialogValue(false)
            },
    )
}

@Preview(showBackground = true)
@Composable
private fun AlarmInfoNoAlarmPreview() {
    AlkaaThemePreview {
        AlarmInfo(
            date = null,
            onRemoveDate = { },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmInfoWithAlarmPreview() {
    AlkaaThemePreview {
        AlarmInfo(
            date = LocalDateTime(year = 2024, month = 12, day = 25, hour = 9, minute = 0),
            onRemoveDate = { },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmIntervalSelectionPreview() {
    var interval by remember { mutableStateOf(AlarmInterval.DAILY) }

    AlkaaThemePreview {
        AlarmIntervalSelection(
            date = LocalDateTime(year = 2024, month = 12, day = 25, hour = 9, minute = 0),
            alarmInterval = interval,
            onIntervalSelect = { interval = it },
        )
    }
}
