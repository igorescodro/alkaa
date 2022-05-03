package com.escodro.alkaa.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.dialog
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.escodro.alkaa.presentation.home.Home
import com.escodro.category.presentation.bottomsheet.CategoryBottomSheet
import com.escodro.navigation.DestinationArgs
import com.escodro.navigation.DestinationDeepLink
import com.escodro.navigation.Destinations
import com.escodro.preference.presentation.About
import com.escodro.splitinstall.LoadFeature
import com.escodro.task.presentation.add.AddTaskBottomSheet
import com.escodro.task.presentation.detail.main.TaskDetailSection
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.bottomSheet
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

/**
 * Navigation Graph to control the Alkaa navigation.
 *
 * @param startDestination the start destination of the graph
 */
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
@Suppress("LongMethod", "MagicNumber")
@Composable
fun NavGraph(startDestination: String = Destinations.Home) {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)
    val context = LocalContext.current

    val actions = remember(navController) { Actions(navController, context) }

    ModalBottomSheetLayout(bottomSheetNavigator) {
        AnimatedNavHost(navController = navController, startDestination = startDestination) {

            composable(
                route = Destinations.Home,
                deepLinks = listOf(navDeepLink { uriPattern = DestinationDeepLink.HomePattern }),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                }
            ) {
                Home(
                    onTaskClick = actions.openTaskDetail,
                    onAboutClick = actions.openAbout,
                    onTrackerClick = actions.openTracker,
                    onTaskSheetOpen = actions.openTaskBottomSheet,
                    onCategorySheetOpen = actions.openCategoryBottomSheet
                )
            }

            composable(
                route = "${Destinations.TaskDetail}/{${DestinationArgs.TaskId}}",
                arguments = listOf(navArgument(DestinationArgs.TaskId) { type = NavType.LongType }),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = DestinationDeepLink.TaskDetailPattern
                    }
                ),
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Right,
                        animationSpec = tween(700)
                    )
                },
            ) { backStackEntry ->
                val arguments = requireNotNull(backStackEntry.arguments)
                TaskDetailSection(
                    taskId = arguments.getLong(DestinationArgs.TaskId),
                    onUpPress = actions.navigateUp
                )
            }

            composable(
                route = Destinations.About,
            ) {
                About(onUpPress = actions.navigateUp)
            }

            bottomSheet(Destinations.BottomSheet.Task) {
                AddTaskBottomSheet(actions.navigateUp)
            }

            bottomSheet(
                route = "${Destinations.BottomSheet.Category}/{${DestinationArgs.CategoryId}}",
                arguments = listOf(
                    navArgument(DestinationArgs.CategoryId) {
                        type = NavType.LongType
                    }
                ),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = DestinationDeepLink.CategorySheetPattern
                    }
                ),
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getLong(DestinationArgs.CategoryId) ?: 0L
                CategoryBottomSheet(
                    categoryId = id,
                    onHideBottomSheet = actions.navigateUp
                )
            }

            dialog(Destinations.Tracker) {
                LoadFeature(
                    context = context,
                    featureName = FeatureTracker,
                    onDismiss = actions.navigateUp
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
