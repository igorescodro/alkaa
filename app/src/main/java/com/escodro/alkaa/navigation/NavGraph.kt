package com.escodro.alkaa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.escodro.alkaa.presentation.home.Home
import com.escodro.preference.presentation.About
import com.escodro.task.presentation.detail.main.TaskDetailSection

internal object Destinations {
    const val Home = "home"
    const val TaskDetail = "taskDetail"
    const val About = "about"
}

internal object DestinationArgs {
    const val TaskId = "taskId"
}

/**
 * Navigation Graph to control the Alkaa navigation.
 *
 * @param startDestination the start destination of the graph
 */
@Composable
fun NavGraph(startDestination: String = Destinations.Home) {
    val navController = rememberNavController()

    val actions = remember(navController) { Actions(navController) }
    NavHost(navController = navController, startDestination = startDestination) {

        composable(Destinations.Home) {
            Home(
                onTaskClicked = actions.openTaskDetail,
                onAboutClicked = actions.openAbout
            )
        }

        composable(
            "${Destinations.TaskDetail}/{${DestinationArgs.TaskId}}",
            arguments = listOf(navArgument(DestinationArgs.TaskId) { type = NavType.LongType })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            TaskDetailSection(taskId = arguments.getLong(DestinationArgs.TaskId))
        }

        composable(Destinations.About) {
            About()
        }
    }
}

internal data class Actions(val navController: NavHostController) {

    val openTaskDetail: (Long) -> Unit = { taskId ->
        navController.navigate("${Destinations.TaskDetail}/$taskId")
    }

    val openAbout: () -> Unit = {
        navController.navigate(Destinations.About)
    }
}
