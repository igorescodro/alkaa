package com.escodro.permission.di

import org.koin.core.module.Module
import org.koin.dsl.module

val permissionModule = module {
    includes(platformPermissionModule)
}

expect val platformPermissionModule: Module
