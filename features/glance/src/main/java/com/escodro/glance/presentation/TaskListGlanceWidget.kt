package com.escodro.glance.presentation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalGlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.escodro.glance.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class TaskListGlanceWidget : GlanceAppWidget(), KoinComponent {

    private val context: Context by inject()

    private val viewModel: TaskListGlanceDataLoader by inject()

    private val coroutineScope: CoroutineScope = MainScope()

    private var data by mutableStateOf<List<Task>>(emptyList())

    private var glanceId by mutableStateOf<GlanceId?>(null)

    @Composable
    override fun Content() {
        glanceId = LocalGlanceId.current

        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = data.firstOrNull()?.title ?: "Testing",
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
        }
    }

    /**
     * Loads the data and requests the GlanceAppWidget to be updated. This is needed since it is not
     * possible to use traditional compose methods to keep updating an App Widget.
     *
     * For more information about this behavior, please access:
     * https://issuetracker.google.com/issues/211022821
     */
    fun loadData() {
        println("loadData")
        coroutineScope.launch {
            data = viewModel.loadTaskList().first()

            val currentGlanceId = snapshotFlow { glanceId }.firstOrNull()

            if (currentGlanceId != null) {
                update(context, currentGlanceId)
            }
        }
    }

    override suspend fun onDelete(glanceId: GlanceId) {
        super.onDelete(glanceId)
        coroutineScope.cancel()
    }
}
