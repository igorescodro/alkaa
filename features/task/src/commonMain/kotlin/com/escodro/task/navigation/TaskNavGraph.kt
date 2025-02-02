package com.escodro.task.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.escodro.navigation.controller.NavEventController
import com.escodro.navigation.event.Event
import com.escodro.navigation.event.TaskEvent
import com.escodro.navigation.provider.NavGraph
import com.escodro.navigationapi.destination.Destination
import com.escodro.navigationapi.destination.HomeDestination
import com.escodro.navigationapi.destination.TasksDestination
import com.escodro.task.presentation.add.AddTaskBottomSheet
import com.escodro.task.presentation.detail.main.TaskDetailScreen
import com.escodro.task.presentation.list.TaskListSection

internal class TaskNavGraph : NavGraph {

    override val navGraph: NavGraphBuilder.(NavEventController) -> Unit = { navEventController ->
        composable<HomeDestination.TaskList> {
            TaskListSection(
                onItemClick = { id -> navEventController.sendEvent(TaskEvent.OnTaskClick(id)) },
                onAddClick = { navEventController.sendEvent(TaskEvent.OnNewTaskClick) },
            )
        }

        composable<TasksDestination.TaskDetail>(
            enterTransition = { slideInHorizontally() + fadeIn() },
            exitTransition = { slideOutHorizontally() + fadeOut() },
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
