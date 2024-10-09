package com.escodro.permission.di

import com.escodro.permission.PermissionsControllerWrapper
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val permissionModule = module {
    singleOf(::PermissionsControllerWrapper)
}
