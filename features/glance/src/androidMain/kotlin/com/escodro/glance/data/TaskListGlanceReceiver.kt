package com.escodro.glance.data

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.escodro.glance.presentation.TaskListGlanceWidget

internal class TaskListGlanceReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = TaskListGlanceWidget()

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        TaskListUpdaterWorker.enqueue(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        TaskListUpdaterWorker.cancel(context)
    }
}
