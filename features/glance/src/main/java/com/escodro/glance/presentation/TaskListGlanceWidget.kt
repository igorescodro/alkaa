package com.escodro.glance.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.updateAll
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.escodro.glance.R
import com.escodro.glance.model.Task
import com.escodro.navigation.DestinationDeepLink
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class TaskListGlanceWidget : GlanceAppWidget(), KoinComponent {

    private val context: Context by inject()

    private val viewModel: TaskListGlanceViewModel by inject()

    private val coroutineScope: CoroutineScope = MainScope()

    private var taskList by mutableStateOf<List<Task>>(emptyList())

    @OptIn(ExperimentalUnitApi::class)
    @Composable
    override fun Content() {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .cornerRadius(12.dp)
                .appWidgetBackground()
                .background(color = MaterialTheme.colors.background)
                .padding(8.dp)
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth().height(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    provider = ImageProvider(R.drawable.ic_alkaa_icon),
                    contentDescription = "",
                    modifier = GlanceModifier.size(32.dp)
                        .clickable(actionStartActivity(getHomeIntent()))
                )
            }
            if (taskList.isEmpty()) {
                EmptyListContent()
            } else {
                TaskListContent()
            }
        }
    }

    @Composable
    private fun EmptyListContent() {
        Box(modifier = GlanceModifier.fillMaxSize().padding(top = 16.dp)) {
            Text(text = "No tasks")
        }
    }

    @Composable
    private fun TaskListContent() {
        LazyColumn(modifier = GlanceModifier.padding(top = 12.dp)) {
            items(items = taskList) { task ->
                TaskItem(task = task)
            }
        }
    }

    @OptIn(ExperimentalUnitApi::class)
    @Suppress("MagicNumber")
    @Composable
    private fun TaskItem(task: Task) {
        Column(
            modifier = GlanceModifier
                .height(32.dp)
                .fillMaxWidth()
                .clickable(actionStartActivity(getTaskIntent(task.id))),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = GlanceModifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CheckBox(
                    checked = false,
                    onCheckedChange = actionRunCallback<UpdateTaskStatusAction>(
                        actionParametersOf(TaskIdKey to task.id.toString())
                    ),
                    modifier = GlanceModifier.size(32.dp)
                )
                Text(
                    text = task.title,
                    modifier = GlanceModifier
                        .padding(start = 4.dp, end = 8.dp)
                        .fillMaxWidth()
                        .height(24.dp),
                    style = TextStyle(
                        color = ColorProvider(Color.DarkGray),
                        fontSize = TextUnit(14f, TextUnitType.Sp),
                    ),
                    maxLines = 1
                )
            }
        }
    }

    private fun getHomeIntent(): Intent =
        Intent(Intent.ACTION_VIEW, DestinationDeepLink.getTaskHomeUri())

    private fun getTaskIntent(taskId: Long): Intent =
        Intent(Intent.ACTION_VIEW, DestinationDeepLink.getTaskDetailUri(taskId))

    /**
     * Loads the data and requests the GlanceAppWidget to be updated. This is needed since it is not
     * possible to use traditional compose methods to keep updating an App Widget.
     *
     * For more information about this behavior, please access:
     * https://issuetracker.google.com/issues/211022821
     */
    fun loadData() {
        coroutineScope.launch {
            taskList = viewModel.loadTaskList().first()
            updateAll(context)
        }
    }

    override suspend fun onDelete(glanceId: GlanceId) {
        super.onDelete(glanceId)
        coroutineScope.cancel()
    }
}
