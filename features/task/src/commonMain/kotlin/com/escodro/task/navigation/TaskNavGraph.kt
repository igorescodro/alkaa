package com.escodro.task.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.escodro.designsystem.animation.SlideInHorizontallyTransition
import com.escodro.designsystem.animation.SlideOutHorizontallyTransition
import com.escodro.navigationapi.controller.NavEventController
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TasksDestination
import com.escodro.navigationapi.event.Event
import com.escodro.navigationapi.event.TaskEvent
import com.escodro.navigationapi.provider.NavGraph
import com.escodro.task.presentation.add.AddTaskBottomSheet
import com.escodro.task.presentation.detail.main.TaskDetailScreen
import com.escodro.task.presentation.list.TaskListSection

internal class TaskNavGraph : NavGraph {

    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.TaskList> {
            TaskListSection(
                onFabClick = { navEventController.sendEvent(TaskEvent.OnNewTaskClick) },
            )
        }

        composable<TasksDestination.TaskDetail>(
            enterTransition = { SlideInHorizontallyTransition },
            exitTransition = { SlideOutHorizontallyTransition },
            deepLinks = listOf(
                navDeepLink<TasksDestination.TaskDetail>(
                    basePath = "${Destination.URI}/task/{taskId}",
                ),
            ),
        ) { navEntry ->
            val route: TasksDestination.TaskDetail = navEntry.toRoute()
            TaskDetailScreen(
                taskId = route.taskId,
                onUpPress = { navEventController.sendEvent(Event.OnBack) },
            )
        }

        dialog<TasksDestination.AddTaskBottomSheet> {
            AddTaskBottomSheet(
                onHideBottomSheet = { navEventController.sendEvent(Event.OnBack) },
            )
        }
    }
}
