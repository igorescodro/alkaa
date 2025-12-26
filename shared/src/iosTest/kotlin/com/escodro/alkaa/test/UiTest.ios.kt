package com.escodro.alkaa.test

import androidx.compose.runtime.Composable
import org.koin.core.module.Module
import org.koin.dsl.module

actual abstract class AlkaaBaseTest actual constructor()

actual val platformModule: Module
    get() = module { }
