package com.escodro.theme.temp

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry

/**
 * Returns an existing
 * [HiltViewModel](https://dagger.dev/api/latest/dagger/hilt/android/lifecycle/HiltViewModel)
 * -annotated [ViewModel] or creates a new one scoped to the current navigation graph present on
 * the {@link NavController} back stack:
 * ```
 * @Composable
 * fun ExampleScreen() {
 *     val viewmodel = hiltNavGraphViewModel<ExampleViewModel>()
 * }
 * ```
 *
 * If no navigation graph is currently present then the current scope will be used, usually, a
 * fragment or an activity.
 */
@ExperimentalComposeUiApi
@Composable
inline fun <reified VM : ViewModel> hiltNavGraphViewModel(): VM {
    val owner = LocalViewModelStoreOwner.current
    return if (owner is NavBackStackEntry) {
        hiltNavGraphViewModel(owner)
    } else {
        viewModel()
    }
}

/**
 * Returns an existing
 * [HiltViewModel](https://dagger.dev/api/latest/dagger/hilt/android/lifecycle/HiltViewModel)
 * -annotated [ViewModel] or creates a new one scoped to the current navigation graph present on
 * the [NavController] back stack:
 * ```
 * @Composable
 * fun MyApp() {
 *     // ...
 *     NavHost(navController, startDestination = startRoute) {
 *         composable("example") { backStackEntry ->
 *             val viewmodel = hiltNavGraphViewModel<ExampleViewModel>(backStackEntry)
 *         }
 *     }
 * }
 * ```
 *
 * @param backStackEntry The entry of a [NavController] back stack.
 */
@ExperimentalComposeUiApi
@Composable
inline fun <reified VM : ViewModel> hiltNavGraphViewModel(backStackEntry: NavBackStackEntry): VM {
    val viewModelFactory = HiltViewModelFactory(
        context = LocalContext.current,
        navBackStackEntry = backStackEntry
    )
    return ViewModelProvider(backStackEntry, viewModelFactory).get(VM::class.java)
}
