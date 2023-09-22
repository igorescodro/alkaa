package com.escodro.alkaa.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.escodro.alkaa.presentation.home.Home
import com.escodro.navigation.DestinationArgs
import com.escodro.navigation.Destinations
import com.escodro.preference.presentation.About
import com.escodro.preference.presentation.OpenSource
import com.escodro.splitinstall.LoadFeature
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

/**
 * Navigation Graph to control the Alkaa navigation.
 *
 * @param windowSizeClass the window size class from current device
 * @param startDestination the start destination of the graph
 */
@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun NavGraph(windowSizeClass: WindowSizeClass, startDestination: String = Destinations.Home) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberNavController(bottomSheetNavigator)
    val context = LocalContext.current

    val actions = remember(navController) { Actions(navController, context) }

    ModalBottomSheetLayout(bottomSheetNavigator) {
        NavHost(navController = navController, startDestination = startDestination) {
            homeGraph(windowSizeClass, actions)
            taskGraph()
            preferencesGraph(actions)
            categoryGraph()
            trackerGraph(context, actions)
        }
    }
}

@Suppress("MagicNumber")
private fun NavGraphBuilder.homeGraph(windowSizeClass: WindowSizeClass, actions: Actions) {
    composable(
        route = Destinations.Home,
        // deepLinks = listOf(navDeepLink { uriPattern = DestinationDeepLink.HomePattern }),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700),
            )
        },
    ) {
        Home(
            windowSizeClass = windowSizeClass,
            onTaskClick = actions.openTaskDetail,
            onAboutClick = actions.openAbout,
            onTrackerClick = actions.openTracker,
            onOpenSourceClick = actions.openOpenSourceLicense,
            onTaskSheetOpen = actions.openTaskBottomSheet,
            onCategorySheetOpen = actions.openCategoryBottomSheet,
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Suppress("MagicNumber")
private fun NavGraphBuilder.taskGraph() {
    composable(
        route = "${Destinations.TaskDetail}/{${DestinationArgs.TaskId}}",
        arguments = listOf(navArgument(DestinationArgs.TaskId) { type = NavType.LongType }),
        deepLinks = listOf(
            // navDeepLink {
            //     uriPattern = DestinationDeepLink.TaskDetailPattern
            // },
        ),
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(700),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(700),
            )
        },
    ) { backStackEntry ->
        val arguments = requireNotNull(backStackEntry.arguments)
        // TaskDetailSection(
        //     taskId = arguments.getLong(DestinationArgs.TaskId),
        //     onUpPress = actions.navigateUp,
        // )
    }

    bottomSheet(Destinations.BottomSheet.Task) {
        // AddTaskBottomSheet(actions.navigateUp)
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Suppress("MagicNumber")
private fun NavGraphBuilder.categoryGraph() {
    bottomSheet(
        route = "${Destinations.BottomSheet.Category}/{${DestinationArgs.CategoryId}}",
        arguments = listOf(
            navArgument(DestinationArgs.CategoryId) {
                type = NavType.LongType
            },
        ),
        deepLinks = listOf(
            // navDeepLink {
            //     uriPattern = DestinationDeepLink.CategorySheetPattern
            // },
        ),
    ) { backStackEntry ->
        // val id = backStackEntry.arguments?.getLong(DestinationArgs.CategoryId) ?: 0L
        // CategoryBottomSheet(
        //     categoryId = id,
        //     onHideBottomSheet = actions.navigateUp,
        // )
    }
}

@Suppress("MagicNumber")
private fun NavGraphBuilder.preferencesGraph(actions: Actions) {
    composable(route = Destinations.About) {
        About(onUpPress = actions.navigateUp)
    }

    composable(route = Destinations.OpenSource) {
        OpenSource(onUpPress = actions.navigateUp)
    }
}

private fun NavGraphBuilder.trackerGraph(
    context: Context,
    actions: Actions,
) {
    dialog(Destinations.Tracker) {
        LoadFeature(
            context = context,
            featureName = FeatureTracker,
            onDismiss = actions.navigateUp,
        ) {
            // Workaround to be able to use Dynamic Feature with Compose
            // https://issuetracker.google.com/issues/183677219
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(TrackerDeepLink)
                `package` = context.packageName
            }
            context.startActivity(intent)
        }
    }
}

internal data class Actions(val navController: NavHostController, val context: Context) {

    val openTaskDetail: (Long) -> Unit = { taskId ->
        navController.navigate("${Destinations.TaskDetail}/$taskId")
    }

    val openAbout: () -> Unit = {
        navController.navigate(Destinations.About)
    }

    val openTracker: () -> Unit = {
        navController.navigate(Destinations.Tracker)
    }

    val openOpenSourceLicense: () -> Unit = {
        navController.navigate(Destinations.OpenSource)
    }

    val openTaskBottomSheet: () -> Unit = {
        navController.navigate(Destinations.BottomSheet.Task)
    }

    val openCategoryBottomSheet: (Long?) -> Unit = { categoryId ->
        val id = categoryId ?: 0L
        navController.navigate("${Destinations.BottomSheet.Category}/$id")
    }

    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}

private const val FeatureTracker = "tracker"

private const val TrackerDeepLink = "app://com.escodro.tracker"
