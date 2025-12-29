package com.escodro.alkaa.test

import org.koin.core.module.Module
import org.koin.dsl.module

actual abstract class AlkaaTest actual constructor()

actual val platformModule: Module
    get() = module { }
