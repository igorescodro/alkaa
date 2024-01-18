package com.escodro.shared.di

import com.escodro.glance.di.glanceModule
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformSharedModule: Module = module {
    includes(glanceModule)
}
