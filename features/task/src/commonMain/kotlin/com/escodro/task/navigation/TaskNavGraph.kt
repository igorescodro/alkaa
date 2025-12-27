package com.escodro.task.navigation

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.escodro.designsystem.animation.SlideInHorizontallyTransition
import com.escodro.designsystem.animation.SlideOutHorizontallyTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TasksDestination
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.event.TaskEvent
import com.escodro.navigationapi.extension.isSinglePane
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.task.presentation.add.AddTaskBottomSheet
import com.escodro.task.presentation.detail.main.TaskDetailScreen
import com.escodro.task.presentation.list.TaskListSection

internal class TaskNavGraph : NavGraph {

    override val navGraph: EntryProviderScope<Destination>.(NavEventController) -> Unit =
        { navEventController ->
            entry<HomeDestination.TaskList> {
                TaskListSection(
                    isSinglePane = currentWindowAdaptiveInfo().windowSizeClass.isSinglePane(),
                    onItemClick = { taskId -> navEventController.sendEvent(TaskEvent.OnTaskClick(taskId)) },
                    onFabClick = { navEventController.sendEvent(TaskEvent.OnNewTaskClick) },
                )
            }

            entry<TasksDestination.TaskDetail>(
                metadata = NavDisplay.transitionSpec { SlideInHorizontallyTransition } +
                    NavDisplay.popTransitionSpec { SlideOutHorizontallyTransition } +
                    NavDisplay.predictivePopTransitionSpec { SlideOutHorizontallyTransition },
            ) { entry ->
                TaskDetailScreen(
                    isSinglePane = true,
                    taskId = entry.taskId,
                    onUpPress = { navEventController.sendEvent(Event.OnBack) },
                )
            }

            entry<TasksDestination.AddTaskBottomSheet>(metadata = DialogSceneStrategy.dialog()) {
                AddTaskBottomSheet(
                    onHideBottomSheet = { navEventController.sendEvent(Event.OnBack) },
                )
            }
        }
}
