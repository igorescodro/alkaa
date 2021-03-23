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
import androidx.navigation.navDeepLink
import com.escodro.alkaa.presentation.home.Home
import com.escodro.navigation.DestinationArgs
import com.escodro.navigation.DestinationDeepLink
import com.escodro.navigation.Destinations
import com.escodro.preference.presentation.About
import com.escodro.task.presentation.detail.main.TaskDetailSection

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
                onTaskClick = actions.openTaskDetail,
                onAboutClick = actions.openAbout
            )
        }

        composable(
            route = "${Destinations.TaskDetail}/{${DestinationArgs.TaskId}}",
            arguments = listOf(navArgument(DestinationArgs.TaskId) { type = NavType.LongType }),
            deepLinks = listOf(navDeepLink { uriPattern = DestinationDeepLink.TaskDetailPattern })
        ) { backStackEntry ->
            val arguments = requireNotNull(backStackEntry.arguments)
            TaskDetailSection(
                taskId = arguments.getLong(DestinationArgs.TaskId),
                onUpPress = actions.onUpPress
            )
        }

        composable(Destinations.About) {
            About(onUpPress = actions.onUpPress)
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

    val onUpPress: () -> Unit = {
        navController.navigateUp()
    }
}
