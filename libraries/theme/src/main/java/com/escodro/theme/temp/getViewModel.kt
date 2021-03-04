package com.escodro.theme.temp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import org.koin.androidx.viewmodel.ViewModelOwner
import org.koin.androidx.viewmodel.koin.getViewModel
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * Workaround to make ViewModel injections work in Koin 2.2.2. This must be removed as soon as the
 * next release fixes this issue.
 *
 * https://github.com/InsertKoinIO/koin/issues/1006
 */
@Composable
inline fun <reified T : ViewModel> getViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): T {
    val owner = LocalViewModelStoreOwner.current.viewModelStore
    return remember {
        GlobalContext.get().getViewModel(
            qualifier,
            owner = { ViewModelOwner.from(owner) },
            parameters = parameters,
        )
    }
}
